"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var common_1 = require('@angular/common');
var CardDirective = (function () {
    function CardDirective() {
        this.viewDetail = new core_1.EventEmitter();
    }
    __decorate([
        core_1.Input()
    ], CardDirective.prototype, "iconClass");
    __decorate([
        core_1.Input()
    ], CardDirective.prototype, "bgClass");
    __decorate([
        core_1.Input()
    ], CardDirective.prototype, "number");
    __decorate([
        core_1.Input()
    ], CardDirective.prototype, "text");
    __decorate([
        core_1.Output()
    ], CardDirective.prototype, "viewDetail");
    CardDirective = __decorate([
        core_1.Component({
            selector: 'card',
            directives: [common_1.CORE_DIRECTIVES],
            styles: [require('./cards.scss')],
            template: "\n  <div class=\"card card-{{bgClass}} card-inverse\">\n    <div class=\"card-header card-{{bgClass}}\">\n      <div class=\"row\">\n        <div class=\"col-xs-3\">\n          <i class=\"{{iconClass}}\"></i>\n        </div>\n        <div class=\"col-xs-9 text-xs-right\">\n          <div class=\"huge\">{{number}}</div>\n          <div>{{text}}</div>\n        </div>\n      </div>\n    </div>\n    <div class=\"card-footer card-default\">\n      <a href=\"javascript:;\" (click)=\"viewDetails.emit()\" class=\"text-{{bgClass}}\">\n        <span class=\"pull-xs-left\">View Details</span>\n        <span class=\"pull-xs-right\"><i class=\"fa fa-arrow-circle-right\"></i></span>\n        <div class=\"clearfix\"></div>\n      </a>\n    </div>\n  </div>\n  "
        })
    ], CardDirective);
    return CardDirective;
}());
exports.CardDirective = CardDirective;
