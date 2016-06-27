import {Component, OnInit, ViewChild} from '@angular/core';
import {Meals} from "../../../resources";
import {RouteParams, Router} from '@ngrx/router';
import {Observable} from "rxjs/Observable";
import {AuthService} from "../../../services/auth.service";
import {MODAL_DIRECTVES, ModalDirective, BS_VIEW_PROVIDERS} from 'ng2-bootstrap/ng2-bootstrap';
import {CORE_DIRECTIVES, FORM_DIRECTIVES, ControlGroup, FormBuilder, Validators, NgForm} from '@angular/common';
import {Users} from "../../../resources/users.resource";
import {SearchOpts} from "../../../resources/searchopts";
import * as _ from 'lodash';

@Component({
  moduleId: module.id,
  template: require('./meal.edit.html'),
  directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, MODAL_DIRECTVES],
  viewProviders: [BS_VIEW_PROVIDERS],
})
export class MealEditComponent implements OnInit {

  meal:any;

  id$:Observable<any>;

  loggedUser:any;

  users:any;

  form:ControlGroup;

  @ViewChild(ModalDirective)
  private deleteModal: ModalDirective;

  constructor(fb:FormBuilder, private _meals:Meals, private _users: Users,  routeParams:RouteParams, private _auth:AuthService, private _router:Router) {
    this.id$ = routeParams.pluck<any>('id');
    _auth.getCurrentUser().then(
      (user) => this.loggedUser = user
    );

    this.form = fb.group({
      'mealDate':['', Validators.compose([Validators.required, Validators.pattern('\\d{4}-\\d{2}-\\d{2}')])],
      'mealTime':['', Validators.compose([Validators.required, Validators.pattern('\\d{2}:\\d{2}:\\d{2}')])],
      'calories':['',Validators.compose([Validators.required, Validators.pattern('\\d+')])],
      'description':['']
    })
  }

  ngOnInit() {
    let useropts = new SearchOpts();
    useropts.projection='select';
    useropts.size = 999999;
    useropts.sort = ['fullName,asc'];
    this._users.list(useropts).then((res) => {
      this.users = res._embedded.users;
    });

    this.id$
      .distinctUntilChanged()
      .subscribe((id) => {
        if (!id) return;

        if (id === 'new') {
          this.meal = {};
          return;
        } else {
          id = parseInt(id);
        }

        this._meals.find(id, 'edit').then((res) => {
          this.meal = res;
        });
      })
  }

  save(form:NgForm) {
    if (!this.loggedUser.hasRole('ROLE_ADMIN')) {
      this.meal.user = '/users/' + this.loggedUser.id;
    }

    this._meals.save(this.meal)
      .then((user) => {
        this._router.go('/dashboard/meals/', {saved: true})
      })
      .catch((err) => {
      if (err.status == 400 && err.errors) {
        for (let error of err.errors) {
          form.controls[error.property].setErrors({serverError:true})
        }
      }
    });
  }

  delete() {
    this._meals.remove(this.meal.id)
      .then(() => {
        this.deleteModal.onHidden.subscribe(() => {
          this._router.go('/dashboard/meals', {deleted: true})
        });
        this.deleteModal.hide();
      })
      .catch((err) => {
      if (err.status == 403) {

      }
    });
  }

}
