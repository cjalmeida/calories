import {Injectable, OnInit, Inject} from '@angular/core'
import {HttpClient} from './httpclient.service'
import * as jwtDecode from 'jwt-decode'
import {Observable} from 'rxjs/Observable';
import {Router} from '@ngrx/router'
import 'rxjs/Rx';
import {User} from './user.mixin'

/**
 * Service implement JWT token based authentication.
 */
@Injectable()
export class AuthService {

  private currentUser:Promise<User>;

  constructor(private http:HttpClient, private router:Router) {

    // Subscribe to unauthorized errors and act on them
    http.unauthorizedError.subscribe((err:any) => {
      // remove token and invalidate user
      this.http.setHeader('Authentication', null);
      delete localStorage['jwt'];
      this.currentUser = null;

      // Redirect to login if not already there
      var path = this.router.path();
      if (!path.startsWith('/login')) {
        this.router.go('/login');
      }
    })

  }

  /**
   * Issues a login request. The returned promise succeeds with the logged user
   * or errors on fail.
   * @param email
   * @param password
   * @returns {Promise<Object>}
   */
  login(email:string, password:string):Promise<User> {
    this.http.setHeader('Authentication', null);

    let body = JSON.stringify({username: email, password});
    return this.http
      .post('/login', body)
      .toPromise()
      .then((res:any) => {
        let ret = res.json();
        this.http.setHeader('Authentication', `Bearer ${ret['token']}`);
        localStorage['jwt'] = ret['token'];
        return this.getCurrentUser();
      });
  }

  logout() {
    this.http.setHeader('Authentication', null);
    delete localStorage['jwt'];
    this.currentUser = null;
    this.router.go('/login');
  }

  /**
   * Returns a promise with the currently logged user. If not cached, tries to
   * fetch the user using session stored credentials. Fail if not possible to
   * retrieve a valid user.
   * @returns {Promise<User>}
   */
  getCurrentUser():Promise<User> {
    if (this.currentUser) {
      return Promise.resolve<Object>(this.currentUser);
    }

    // Try to fetch user if we have a valid token.
    if (this.hasValidToken()) {
      return this.currentUser = this.http
        .get('/users/search/self?projection=simple')
        .map((res) => new User(res.json()))
        .toPromise();

    } else {
      return Promise.reject<Object>({cause: "No valid token found."});
    }
  }


  /**
   * Returns true if we have a valid token.
   * @returns {boolean}
   */
  hasValidToken():boolean {
    // Check we have token
    let token = localStorage['jwt'];
    if (!token) return false;

    // Check expiration
    let data = jwtDecode(token);
    let now = new Date().getTime() / 1000;
    if (now > data['exp']) return false;

    // Assume the token is good
    this.http.setHeader('Authentication', `Bearer ${token}`);
    return true;
  }
}
