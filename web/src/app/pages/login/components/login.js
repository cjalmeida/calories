"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var LoginComponent = (function () {
    function LoginComponent(authService, router) {
        this.authService = authService;
        this.router = router;
        this.email = '';
        this.password = '';
        this.error = null;
    }
    LoginComponent.prototype.login = function () {
        var _this = this;
        this.error = null;
        this.authService
            .login(this.email, this.password)
            .then(function () {
            _this.router.go('/dashboard/home');
        })
            .catch(function (err) {
            _this.error = 'Incorrect email or password.';
        });
    };
    LoginComponent = __decorate([
        core_1.Component({
            selector: 'login-cmp',
            template: require('./login.html'),
            styles: [require('./login.scss')]
        })
    ], LoginComponent);
    return LoginComponent;
}());
exports.LoginComponent = LoginComponent;
