import {Component, ViewEncapsulation} from '@angular/core';
import {TopNavComponent} from './topnav';
import {SidebarComponent} from './sidebar';
import {AuthService} from '../../../services/index'

@Component({
  moduleId: module.id,
  selector: 'dashboard-cmp',
  template: require('./dashboard.html'),
  encapsulation: ViewEncapsulation.None,
  directives: [TopNavComponent, SidebarComponent],
  providers: []
})

export class DashboardComponent {

  constructor(public auth: AuthService) {

  }
}
