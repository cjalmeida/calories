import 'rxjs/add/observable/of';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Guard, Router, TraversalCandidate, LocationChange} from '@ngrx/router';
import {AuthService} from './auth.service'

@Injectable()
export class AuthGuard implements Guard {
  constructor(private auth: AuthService, private router: Router) {
  }

  protectRoute(candidate:TraversalCandidate) {
    return Observable.of(this.auth.hasValidToken());
  }
}
