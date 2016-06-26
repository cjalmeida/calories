import {Component, OnInit} from '@angular/core';
import {Users, User, Roles} from "../../../resources";
import {RouteParams, Router} from '@ngrx/router';
import {Observable} from "rxjs/Observable";
import * as _ from 'lodash';
import {AuthService} from "../../../services/auth.service";
import {MODAL_DIRECTVES, BS_VIEW_PROVIDERS} from 'ng2-bootstrap/ng2-bootstrap';

@Component({
  moduleId: module.id,
  template: require('./user.edit.html'),
  directives: [MODAL_DIRECTVES],
  viewProviders:[BS_VIEW_PROVIDERS],
})
export class UserEditComponent implements OnInit {

  user:User;
  roles:[{_links:any, selected}];

  id$:Observable<any>;

  loggedUser: any;

  hasError:boolean = false;

  constructor(private _users:Users, private _roles:Roles, routeParams:RouteParams, private _auth:AuthService, private _router: Router) {
    this.id$ = routeParams.pluck<any>('id');
    _auth.getCurrentUser().then(
      (user) => this.loggedUser = user
    );
  }

  ngOnInit() {
    this.id$
      .distinctUntilChanged()
      .subscribe((id) => {
        if (!id) return;

        if (id === 'new') {
          this.user = new User();
          return;
        } else {
          id = parseInt(id);
        }

        let userP = this._users.find(id).then((res) => {
          this.user = res;
          this.user.password = null;
        });

        let rolesP = this._roles.list().then((res) => {
          this.roles = res._embedded.roles;
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

  save() {
    this.user.roles = _.chain(this.roles)
      .filter((r) => r.selected)
      .map((r) => r._links.self.href)
      .value() as [any];

    this._users.save(this.user)
    .then((user) => {
        this._router.go('/dashboard/users/', {saved:true})
      })
    .catch((err) => {
      this.hasError = true;
      if (err.status == 400 && err.errors) {
        for (let error of err.errors) {
          document.querySelector(`*[data-prop="${error.property}"].field-group`)
            .classList.add('error');
        }
      }
    });
  }

  delete() {
    this._users.remove(this.user.id)
      .then(() => {
        this._router.go('/dashboard/users', {deleted: true})
      })
      .catch((err) => {
        if (err.status == 403) {

        }
      });
  }

}
