"use strict";
var common_1 = require('@angular/common');
var core_1 = require('@angular/core');
var platform_browser_dynamic_1 = require('@angular/platform-browser-dynamic');
var http_1 = require('@angular/http');
var app_component_1 = require('./app/layouts/base/app.component');
// Configure routes
var index_1 = require('./app/services/index');
var router_1 = require('@ngrx/router');
var dashboard_1 = require('./app/layouts/dashboard/components/dashboard');
var home_1 = require('./app/pages/home/components/home');
var home_2 = require('./app/pages/home2/components/home');
var charts_1 = require('./app/pages/charts/components/charts');
var grid_1 = require('./app/pages/grid/components/grid');
var forms_1 = require('./app/pages/forms/components/forms');
var tables_1 = require('./app/pages/tables/components/tables');
var bs_element_1 = require('./app/pages/bootstrap-element/components/bs_element');
var blank_page_1 = require('./app/pages/blank-page/components/blank_page');
var component_1 = require('./app/pages/component/components/component');
var login_1 = require('./app/pages/login/components/login');
var config_1 = require('./config');
require('./styles/main.scss');
var routes = [
    {
        path: '/dashboard', component: dashboard_1.DashboardComponent,
        guards: [index_1.AuthGuard],
        children: [
            { path: '/home', component: home_1.HomeComponent },
            { path: '/home2', component: home_2.Home2Component },
            { path: '/chart', component: charts_1.ChartComponent },
            { path: '/table', component: tables_1.TableComponent },
            { path: '/forms', component: forms_1.FormComponent },
            { path: '/element', component: bs_element_1.BSElementComponent },
            { path: '/grid', component: grid_1.GridComponent },
            { path: '/component', component: component_1.NG2Component },
            { path: '/blank-page', component: blank_page_1.BlankPageComponent },
        ]
    },
    { path: '/login', component: login_1.LoginComponent },
    { path: '/', redirectTo: '/dashboard/home' }
];
if ('<%= ENV %>' === 'prod') {
    core_1.enableProdMode();
}
/**
 * Bootstraps the application and makes the ROUTER_PROVIDERS and the APP_BASE_HREF available to it.
 * @see https://angular.io/docs/ts/latest/api/platform-browser-dynamic/index/bootstrap-function.html
 */
platform_browser_dynamic_1.bootstrap(app_component_1.AppComponent, [
    http_1.HTTP_PROVIDERS, index_1.AuthService, index_1.HttpClient,
    router_1.provideRouter(routes),
    core_1.provide(common_1.APP_BASE_HREF, { useValue: '/' }),
    core_1.provide('config', { useValue: config_1.config })
]);
