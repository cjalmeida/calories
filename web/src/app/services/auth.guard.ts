import 'rxjs/add/observable/of';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Guard, Router, TraversalCandidate, LocationChange} from '@ngrx/router';
import {AuthService} from './auth.service'

/**
 * ngrx/router guard to avoid loading secured pages.
 */
@Injectable()
export class AuthGuard implements Guard {

  constructor(private auth: AuthService, private router: Router) {

  }

  protectRoute(candidate:TraversalCandidate):Observable<boolean> {
    let obs = Observable.of(this.auth.hasValidToken());

    obs.distinctUntilChanged()
      .subscribe((val) => {
        if (val == false) {
          console.log('No token');
          this.router.go('/login');
        }
      });

    return obs;
  }
}
