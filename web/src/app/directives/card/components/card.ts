import {Component, Input, Output, EventEmitter} from '@angular/core';
import {CORE_DIRECTIVES} from '@angular/common';

@Component({
  selector: 'card',
  directives: [CORE_DIRECTIVES],
  styles: [require('./cards.scss')],
  template: `
  <div class="card card-{{bgClass}} card-inverse">
    <div class="card-header card-{{bgClass}}">
      <div class="row">
        <div class="col-xs-3">
          <i class="{{iconClass}}"></i>
        </div>
        <div class="col-xs-9 text-xs-right">
          <div class="huge">{{number}}</div>
          <div>{{text}}</div>
        </div>
      </div>
    </div>
    <div class="card-footer card-default">
      <a href="javascript:;" (click)="click && click.emit()" class="text-{{bgClass}}">
        <span class="pull-xs-left">View Details</span>
        <span class="pull-xs-right"><i class="fa fa-arrow-circle-right"></i></span>
        <div class="clearfix"></div>
      </a>
    </div>
  </div>
  `,
})

export class CardDirective {
  @Input() iconClass;
  @Input() bgClass;
  @Input() number;
  @Input() text;
  @Output() click = new EventEmitter();
}
