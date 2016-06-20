"use strict";
var _ = require('lodash');
var User = (function () {
    function User(json) {
        if (json === void 0) { json = null; }
        if (json) {
            _.assign(this, json);
        }
    }
    User.prototype.hasRole = function (role) {
        return this.roles.indexOf(role) > -1;
    };
    return User;
}());
exports.User = User;
