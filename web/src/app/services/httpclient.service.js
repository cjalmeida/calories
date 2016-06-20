"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var core_1 = require('@angular/core');
var http_1 = require('@angular/http');
var Subject_1 = require('rxjs/Subject');
var HttpClient = (function () {
    function HttpClient(http, config) {
        this.http = http;
        this.config = config;
        this.headers = { 'Content-Type': 'application/json' };
        this.unauthorizedError = new Subject_1.Subject();
        this.base = config['API_BASE'].replace(/\/+$/, '');
    }
    HttpClient.prototype.setHeader = function (key, val) {
        if (!val) {
            delete this.headers[key];
        }
        else {
            this.headers[key] = val;
        }
    };
    HttpClient.prototype.mergeHeaders = function (options) {
        if (!options)
            return;
        options.headers = options.headers || new http_1.Headers();
        for (var k in this.headers) {
            options.headers.set(k, this.headers[k]);
        }
    };
    HttpClient.prototype.request = function (path, options) {
        var _this = this;
        this.mergeHeaders(options);
        var _call = this.http.request(this.base + path, options);
        _call.subscribe(null, function (err) {
            if (err.status == 401) {
                // Notify the 401 error
                _this.unauthorizedError.next(err);
            }
        });
        return _call;
    };
    HttpClient.prototype.get = function (path, options) {
        options = options || {};
        options.method = 'GET';
        return this.request(path, options);
    };
    HttpClient.prototype.post = function (path, body, options) {
        options = options || {};
        options.method = 'POST';
        options.body = body;
        return this.request(path, options);
    };
    HttpClient.prototype.put = function (path, body, options) {
        options = options || {};
        options.method = 'PUT';
        options.body = body;
        return this.request(path, options);
    };
    HttpClient.prototype.delete = function (path, options) {
        options = options || {};
        options.method = 'DELETE';
        return this.request(path, options);
    };
    HttpClient = __decorate([
        core_1.Injectable(),
        __param(1, core_1.Inject('config'))
    ], HttpClient);
    return HttpClient;
}());
exports.HttpClient = HttpClient;
