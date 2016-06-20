import {APP_BASE_HREF} from '@angular/common';
import {enableProdMode, provide} from '@angular/core';
import {bootstrap} from '@angular/platform-browser-dynamic';
import {HTTP_PROVIDERS} from '@angular/http';
import {AppComponent} from './app/layouts/base/app.component';
import {AuthGuard, AuthService, HttpClient} from './app/services/index'
import {Routes, provideRouter} from '@ngrx/router';
import {DashboardComponent} from './app/layouts/dashboard/components/dashboard';
import {HomeComponent} from './app/pages/home/components/home';
import {LoginComponent} from './app/pages/login/components/login';
import {SignupComponent} from "./app/pages/login/components/signup";
import {Meals, Users, Roles} from './app/resources';
import {UserListComponent} from "./app/pages/users/components/user.list";
import {UserEditComponent} from "./app/pages/users/components/user.edit";
import {config} from './config';
import './styles/main.scss'


const routes:Routes = [
  {
    path: '/dashboard', component: DashboardComponent,
    guards: [AuthGuard],
    children: [
      {path: '/home', component: HomeComponent},
      {path: '/users/:id', component: UserEditComponent},
      {path: '/users', component: UserListComponent},
      {path: '/meals', component: HomeComponent},
    ]
  },
  { path: '/login', component: LoginComponent},
  { path: '/signup', component: SignupComponent},
  { path: '/', redirectTo: '/dashboard/home'}
];


/**
 * Bootstraps the application and makes the ROUTER_PROVIDERS and the APP_BASE_HREF available to it.
 * @see https://angular.io/docs/ts/latest/api/platform-browser-dynamic/index/bootstrap-function.html
 */
bootstrap(AppComponent, [
  HTTP_PROVIDERS, AuthService, HttpClient, Meals, Users, Roles,
  provideRouter(routes),
  provide(APP_BASE_HREF, {useValue: '/'}),
  provide('config', {useValue: config})
]);
