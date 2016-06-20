import { Component } from '@angular/core';
import { CORE_DIRECTIVES, FORM_DIRECTIVES } from '@angular/common';
import {AuthService} from "../../../services/auth.service";
import {User} from "../../../services/user.mixin";

@Component({
	moduleId: module.id,
	selector: 'sidebar-cmp',
	template: require('./sidebar.html'),
	directives: [CORE_DIRECTIVES, FORM_DIRECTIVES]
})

export class SidebarComponent {

  user:User;

  constructor(private auth: AuthService) {
    auth.getCurrentUser().then((user) => this.user = user);
  }

	isActive = false;
	showMenu: string = '';
	eventCalled() {
		this.isActive = !this.isActive;
	}
	addExpandClass(element: any) {
		if (element === this.showMenu) {
			this.showMenu = '0';
		} else {
			this.showMenu = element;
		}
	}
}
