import {Component, OnInit} from '@angular/core';
import {Meals, Users} from "../../../resources";
import {Observable} from "rxjs/Observable";
import {QueryParams} from '@ngrx/router';

@Component({
  moduleId: module.id,
  template: require('./user.list.html')
})
export class UserListComponent implements OnInit {

  res:{_embedded:{users:[any]}, page:any};

  saved$: Observable<boolean>;

  constructor(private users: Users, routeParams:QueryParams) {
    this.saved$ = routeParams.pluck<boolean>('saved');
  }

  ngOnInit() {
    this.users.list().then((res) => {
      this.res = res;
    })
  }



}
