import {Component, ViewEncapsulation} from '@angular/core';
import {CORE_DIRECTIVES} from '@angular/common';
import {DROPDOWN_DIRECTIVES} from 'ng2-bootstrap/ng2-bootstrap';
import {AuthService} from '../../../services/index';
import {Router} from '@ngrx/router';

@Component({
  moduleId: module.id,
  selector: 'top-nav',
  template: require('./topnav.html'),
  encapsulation: ViewEncapsulation.None,
  directives: [DROPDOWN_DIRECTIVES, CORE_DIRECTIVES],
})

export class TopNavComponent {

  user: any;

  constructor(public auth: AuthService, public router:Router) {
    auth.getCurrentUser().then(
      (user) => this.user = user
    );
  }

}
