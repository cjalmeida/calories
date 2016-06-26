import {Component, OnInit} from '@angular/core';
import {Meals, SearchOpts} from "../../../resources";
import {Observable} from "rxjs/Observable";
import {QueryParams} from '@ngrx/router';
import {AuthService} from "../../../services/auth.service";
import {User} from "../../../services/user.mixin";

@Component({
  moduleId: module.id,
  template: require('./meals.list.html')
})
export class MealListComponent implements OnInit {

  res:{_embedded:{meals:[any]}, page:any};

  saved$: Observable<boolean>;
  deleted$: Observable<boolean>;

  search = new SearchOpts();

  loggedUser: User;

  constructor(private meals: Meals, routeParams:QueryParams, auth:AuthService) {
    this.saved$ = routeParams.pluck<boolean>('saved');
    this.deleted$ = routeParams.pluck<boolean>('deleted');
    this.search.projection = 'list';
    auth.getCurrentUser().then(
      (user) => this.loggedUser = user
    );
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

}
