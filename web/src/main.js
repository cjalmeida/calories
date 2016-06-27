"use strict";
var common_1 = require('@angular/common');
var core_1 = require('@angular/core');
var platform_browser_dynamic_1 = require('@angular/platform-browser-dynamic');
var http_1 = require('@angular/http');
var app_component_1 = require('./app/layouts/base/app.component');
var index_1 = require('./app/services/index');
var router_1 = require('@ngrx/router');
var dashboard_1 = require('./app/layouts/dashboard/components/dashboard');
var home_1 = require('./app/pages/home/components/home');
var login_1 = require('./app/pages/login/components/login');
var signup_1 = require("./app/pages/login/components/signup");
var resources_1 = require('./app/resources');
var user_list_1 = require("./app/pages/users/components/user.list");
var user_edit_1 = require("./app/pages/users/components/user.edit");
var config_1 = require('./config');
require('./styles/main.scss');
var meal_list_1 = require("./app/pages/meals/components/meal.list");
var meal_edit_1 = require("./app/pages/meals/components/meal.edit");
var routes = [
    {
        path: '/dashboard', component: dashboard_1.DashboardComponent,
        guards: [index_1.AuthGuard],
        children: [
            { path: '/home', component: home_1.HomeComponent },
            { path: '/users/:id', component: user_edit_1.UserEditComponent },
            { path: '/users', component: user_list_1.UserListComponent },
            { path: '/meals/:id', component: meal_edit_1.MealEditComponent },
            { path: '/meals', component: meal_list_1.MealListComponent },
        ]
    },
    { path: '/login', component: login_1.LoginComponent },
    { path: '/signup', component: signup_1.SignupComponent },
    { path: '/', redirectTo: '/dashboard/home' }
];
/**
 * Bootstraps the application and makes the ROUTER_PROVIDERS and the APP_BASE_HREF available to it.
 * @see https://angular.io/docs/ts/latest/api/platform-browser-dynamic/index/bootstrap-function.html
 */
platform_browser_dynamic_1.bootstrap(app_component_1.AppComponent, [
    http_1.HTTP_PROVIDERS, index_1.AuthService, index_1.HttpClient, resources_1.Meals, resources_1.Users, resources_1.Roles, index_1.AuthGuard,
    router_1.provideRouter(routes),
    core_1.provide(common_1.APP_BASE_HREF, { useValue: '/' }),
    core_1.provide('config', { useValue: config_1.config })
]);
//# sourceMappingURL=main.js.map