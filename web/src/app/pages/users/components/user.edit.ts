import {Component, OnInit} from '@angular/core';
import {Users, Roles} from "../../../resources";
import {RouteParams, Router} from '@ngrx/router';
import {Observable} from "rxjs/Observable";
import * as _ from 'lodash';
import {AuthService} from "../../../services/auth.service";

@Component({
  moduleId: module.id,
  template: require('./user.edit.html')
})
export class UserEditComponent implements OnInit {

  user:{roles:[any], password:string};
  roles:[{_links:any, selected}];

  id$:Observable<number>;

  loggedUser: any;

  constructor(private _users:Users, private _roles:Roles, routeParams:RouteParams, private _auth:AuthService, private _router: Router) {
    this.id$ = routeParams.pluck<number>('id');
    _auth.getCurrentUser().then(
      (user) => this.loggedUser = user
    );
  }

  ngOnInit() {
    this.id$
      .distinctUntilChanged()
      .subscribe((id) => {
        if (!id) return;

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

  submit() {
    this.user.roles = _.chain(this.roles)
      .filter((r) => r.selected)
      .map((r) => r._links.self.href)
      .value() as [any];

    this._users.save(this.user).then(
      (user) => {
        this._router.go('/dashboard/users/', {saved:true})
      });
  }

}
