import {Component, OnInit} from '@angular/core';
import {Meals} from "../../../resources";
import {RouteParams, Router} from '@ngrx/router';
import {Observable} from "rxjs/Observable";
import * as _ from 'lodash';
import {AuthService} from "../../../services/auth.service";
import {MODAL_DIRECTVES, BS_VIEW_PROVIDERS} from 'ng2-bootstrap/ng2-bootstrap';

@Component({
  moduleId: module.id,
  template: require('./meals.edit.html'),
  directives: [MODAL_DIRECTVES],
  viewProviders: [BS_VIEW_PROVIDERS],
})
export class MealEditComponent implements OnInit {

  meal:any;

  id$:Observable<any>;

  loggedUser:any;

  hasError:boolean = false;

  constructor(private _meals:Meals, routeParams:RouteParams, private _auth:AuthService, private _router:Router) {
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
          this.meal = {};
          return;
        } else {
          id = parseInt(id);
        }

        this._meals.find(id).then((res) => {
          this.meal = res;
        });
      })
  }

  save() {
    this.meal.user = '/users/' + this.loggedUser.id;

    this._meals.save(this.meal)
      .then((user) => {
        this._router.go('/dashboard/meals/', {saved: true})
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
    this._meals.remove(this.meal.id)
      .then(() => {
        this._router.go('/dashboard/meals', {deleted: true})
      })
      .catch((err) => {
      if (err.status == 403) {

      }
    });
  }

}
