import {Component, OnInit, ViewChild, ContentChild} from '@angular/core';
import {NgForm, ControlGroup, FormBuilder, Validators, Control} from '@angular/common';
import {Users, User, Roles} from "../../../resources";
import {RouteParams, Router} from '@ngrx/router';
import {Observable} from "rxjs/Observable";
import * as _ from 'lodash';
import {AuthService} from "../../../services/auth.service";
import {MODAL_DIRECTVES, BS_VIEW_PROVIDERS, ModalDirective} from 'ng2-bootstrap/ng2-bootstrap';
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from '@angular/common';
import {extractId} from "../../../resources/utils";

@Component({
  moduleId: module.id,
  template: require('./user.edit.html'),
  directives: [MODAL_DIRECTVES, CORE_DIRECTIVES, FORM_DIRECTIVES],
  viewProviders: [BS_VIEW_PROVIDERS],
})
export class UserEditComponent implements OnInit {

  user:User;
  roles:[{_links:any, selected}];

  id$:Observable<any>;

  loggedUser:any;

  form:ControlGroup;

  @ViewChild(ModalDirective)
  private deleteModal:ModalDirective;


  constructor(fb:FormBuilder, private _users:Users, private _roles:Roles, routeParams:RouteParams, private _auth:AuthService, private _router:Router) {
    this.id$ = routeParams.pluck<any>('id');
    _auth.getCurrentUser().then(
      (user) => this.loggedUser = user
    );

    this.form = fb.group({
      'fullName': ['', Validators.required],
      'email': ['', Validators.required],
      'plainPassword': ['', (c) => this.validatePassword(c)],
      'userTimezone': ['', Validators.required],
      'caloriesPerDay': ['', Validators.pattern('[0-9]*')]
    })
  }

  validatePassword(control:Control):any {
    if (!this.user) return null;
    if (!this.user.id && !control.value) {
      return {required: true};
    }
    return null;
  }

  ngOnInit() {
    let rolesP = this._roles.list().then((res) => {
      this.roles = res._embedded.roles;
    });

    this.id$
      .distinctUntilChanged()
      .subscribe((id) => {
        if (!id) return;

        if (id === 'new') {
          this.user = new User();
          this.user.active = true;
          return;
        } else {
          id = parseInt(id);
        }

        let userP = this._users.find(id).then((res) => {
          this.user = res;
          this.user.plainPassword = null;
        });

        // Set the 'roles' 'selected' property to true for existing roles
        Promise.all([userP, rolesP]).then(() => {
          for (let role of this.roles) {
            for (let userRole of this.user.roles) {
              if (role._links.self.href === userRole._links.self.href) {
                role.selected = true;
              }
            }
          }
        })
      });
  }

  save(form:NgForm) {
    let admin = this.loggedUser.hasRole('ROLE_ADMIN', 'ROLE_MANAGER');
    if (admin) {
      this.user.roles = _.chain(this.roles)
        .filter((r) => r.selected)
        .map((r) => r._links.self.href)
        .value() as [any];
    } else {
      delete this.user.active;
      delete this.user.roles;
    }

    if (this.user.plainPassword == null) {
      delete this.user.plainPassword;
    }

    this._users.save(this.user)
      .then((res) => {
        let user = res.json();
        let p = Promise.resolve(user);
        if (user.id === this.loggedUser.id) {
          p = this._auth.reloadUser();
        }
        p.then(() => {
          if (admin) {
            this._router.go('/dashboard/users/', {saved: true})
          } else {
            this._router.go('/dashboard/home/')
          }
        })
      })
      .catch((err) => {
        if (err.status == 400 && err.errors) {
          for (let error of err.errors) {
            form.controls[error.property].setErrors({serverError: true})
          }
        }
      });
  }

  delete() {
    this._users.remove(this.user.id)
      .then(() => {
        this.deleteModal.onHidden.subscribe(() => {
          this._router.go('/dashboard/users', {deleted: true})
        });
        this.deleteModal.hide();
      })
      .catch((err) => {
        if (err.status == 409) {
          alert("Cannot delete user. Possibly has dependent meals.")
        }
        this.deleteModal.hide();
      });
  }

}
