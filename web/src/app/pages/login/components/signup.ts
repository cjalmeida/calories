import {Component} from '@angular/core';
import {Http} from '@angular/http';
import {AuthService} from "../../../services/auth.service";
import {Router} from "@ngrx/router";
import {HttpClient} from "../../../services/httpclient.service";

@Component({
	moduleId: module.id,
	selector: 'signup-cmp',
	template: require('./signup.html'),
  styles: [require('./login.scss')]
})

export class SignupComponent {

  user:any = {};

  constructor(private http:HttpClient, private auth:AuthService, private router:Router) {

  }

  private register() {
    let body = JSON.stringify(this.user);
    this.http.post('/signup', body).toPromise()
      .then((res) => {
        return this.auth.login(this.user.email, this.user.password)
      })
      .then(() => {
        this.router.go('/dashboard/home');
      })
  }
}
