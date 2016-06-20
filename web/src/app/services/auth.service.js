"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var jwtDecode = require('jwt-decode');
require('rxjs/Rx');
var user_mixin_1 = require('./user.mixin');
/**
 * Service implement JWT token based authentication.
 */
var AuthService = (function () {
    function AuthService(http, router) {
        var _this = this;
        this.http = http;
        this.router = router;
        // Subscribe to unauthorized errors and act on them
        http.unauthorizedError.subscribe(function (err) {
            // remove token and invalidate user
            _this.http.setHeader('Authentication', null);
            delete localStorage['jwt'];
            _this.currentUser = null;
            // Redirect to login if not already there
            var path = _this.router.path();
            if (!path.startsWith('/login')) {
                _this.router.go('/login');
            }
        });
    }
    /**
     * Issues a login request. The returned promise succeeds with the logged user
     * or errors on fail.
     * @param email
     * @param password
     * @returns {Promise<Object>}
     */
    AuthService.prototype.login = function (email, password) {
        var _this = this;
        this.http.setHeader('Authentication', null);
        var body = JSON.stringify({ username: email, password: password });
        return this.http
            .post('/login', body)
            .toPromise()
            .then(function (res) {
            var ret = res.json();
            _this.http.setHeader('Authentication', "Bearer " + ret['token']);
            localStorage['jwt'] = ret['token'];
            return _this.getCurrentUser();
        });
    };
    AuthService.prototype.logout = function () {
        this.http.setHeader('Authentication', null);
        delete localStorage['jwt'];
        this.currentUser = null;
        this.router.go('/login');
    };
    /**
     * Returns a promise with the currently logged user. If not cached, tries to
     * fetch the user using session stored credentials. Fail if not possible to
     * retrieve a valid user.
     * @returns {Promise<User>}
     */
    AuthService.prototype.getCurrentUser = function () {
        if (this.currentUser) {
            return Promise.resolve(this.currentUser);
        }
        // Try to fetch user if we have a valid token.
        if (this.hasValidToken()) {
            return this.http
                .get('/users/search/self?projection=simple')
                .map(function (res) { return new user_mixin_1.User(res.json()); })
                .toPromise();
        }
        else {
            return Promise.reject({ cause: "No valid token found." });
        }
    };
    /**
     * Returns true if we have a valid token.
     * @returns {boolean}
     */
    AuthService.prototype.hasValidToken = function () {
        // Check we have token
        var token = localStorage['jwt'];
        if (!token)
            return false;
        // Check expiration
        var data = jwtDecode(token);
        var now = new Date().getTime() / 1000;
        if (now > data['exp'])
            return false;
        // Assume the token is good
        this.http.setHeader('Authentication', "Bearer " + token);
        return true;
    };
    AuthService = __decorate([
        core_1.Injectable()
    ], AuthService);
    return AuthService;
}());
exports.AuthService = AuthService;
