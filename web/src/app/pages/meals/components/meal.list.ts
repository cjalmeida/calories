import {Component, OnInit} from '@angular/core';
import {Meals, SearchOpts} from "../../../resources";
import {Observable} from "rxjs/Observable";
import {QueryParams} from '@ngrx/router';
import {AuthService} from "../../../services/auth.service";
import {User} from "../../../services/user.mixin";
import {CollapseDirective} from 'ng2-bootstrap';

@Component({
  moduleId: module.id,
  template: require('./meal.list.html'),
  directives:[CollapseDirective]
})
export class MealListComponent implements OnInit {

  res:{_embedded:{meals:[any]}, page:any};

  saved$: Observable<boolean>;
  deleted$: Observable<boolean>;

  search:SearchOpts;

  loggedUser: User;

  showFilters:boolean;

  constructor(private meals: Meals, routeParams:QueryParams, auth:AuthService) {
    this.saved$ = routeParams.pluck<boolean>('saved');
    this.deleted$ = routeParams.pluck<boolean>('deleted');
    this.resetFilters();
    auth.getCurrentUser().then(
      (user) => this.loggedUser = user
    );
    this.showFilters = false;
  }

  ngOnInit() {
    this.list();
  }

  list() {
    this.meals.list(this.search).then((res) => {
      this.res = res;
    });
  }

  hasPrev():boolean {
    return this.res && this.res.page.number > 0;
  }

  hasNext():boolean {
    return this.res && (this.res.page.number + 1) < this.res.page.totalPages;
  }

  previous() {
    this.search.page--;
    this.list();
  }

  next() {
    this.search.page++;
    this.list();
  }

  applyFilters() {
    for (let k in this.search) {
      let v = this.search[k]
      if (v === '' || v === undefined || v === null) {
        delete this.search[k];
      }
    }
    this.search.page = 0;
    this.list();
  }

  resetFilters() {
    this.search = new SearchOpts();
    this.search.projection = 'list';
    this.search.sort = ['mealDate,desc', 'mealTime,desc'];
    this.list();
  }

}
