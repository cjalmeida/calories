import {Component, OnInit} from '@angular/core';
import {Meals, Users, User, SearchOpts} from "../../../resources";
import {Observable} from "rxjs/Observable";
import {QueryParams} from '@ngrx/router';

@Component({
  moduleId: module.id,
  template: require('./user.list.html')
})
export class UserListComponent implements OnInit {

  res:{_embedded:{users:[any]}, page:any};

  saved$: Observable<boolean>;
  deleted$: Observable<boolean>;

  search = new SearchOpts();

  constructor(private users: Users, routeParams:QueryParams) {
    this.saved$ = routeParams.pluck<boolean>('saved');
    this.deleted$ = routeParams.pluck<boolean>('deleted');
    this.search.projection = 'list';
  }

  ngOnInit() {
    this.list();
  }

  list() {
    this.users.list(this.search).then((res) => {
      this.res = res;
    });
  }

  hasPrev():boolean {
    return this.res && this.res.page.number > 0;
  }

  hasNext():boolean {
    return this.res && (this.res.page.number + 1) < this.res.page.totalPages;
  }

  prev() {
    this.search.page--;
    this.list();
  }

  next() {
    this.search.page++;
    this.list();
  }

}
