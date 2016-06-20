import {Component} from '@angular/core';
import {Router} from '@ngrx/router';
import {AuthService} from '../../../services/index';
import { CORE_DIRECTIVES, FORM_DIRECTIVES } from '@angular/common';

@Component({
  selector: 'login-cmp',
  template: require('./login.html'),
  styles: [require('./login.scss')],
  directives: [CORE_DIRECTIVES, FORM_DIRECTIVES]
})

export class LoginComponent {
  email:string = '';
  password:string = '';

  error:string = null;

  constructor(private authService:AuthService, private router:Router) {

  }

  login() {
    this.error = null;
    this.authService
      .login(this.email, this.password)
      .then(() => {
        this.router.go('/dashboard/home')
      })
      .catch((err:any) => {
        this.error = 'Incorrect email or password.'
      })
  }
}
