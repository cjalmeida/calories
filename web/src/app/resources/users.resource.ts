import {Injectable} from '@angular/core';
import {HttpClient} from "../services/httpclient.service";
import {extractId} from './utils';
import {SearchOpts} from "./searchopts.ts";
import {User as UserMixin} from "../services/user.mixin.ts";

@Injectable()
export class Users {

  constructor(private http:HttpClient) {

  }

  /**
   * Count number of registered users
   */
  getCount():Promise<any> {
    return this.http
      .get('/reports/userCount')
      .toPromise()
      .then((res) => res.json());
  }

  /**
   * Find a user by id.
   */
  find(id:number):Promise<any> {
    return this.http
      .get(`/users/${id}`)
      .map((res) => {
        let data = res.json();
        data.id = extractId(data);
        return this.http.get(`/users/${id}/roles`)
          .map((resRoles) => {
            data.roles = resRoles.json()._embedded.roles;
            return data;
          })
          .toPromise();
      })
      .toPromise();
  }

  /**
   * List users.
   */
  list(search?: SearchOpts):Promise<any> {
    search = search || new SearchOpts();
    return this.http
      .get('/users', {search: search.toParams()})
      .map((res) => res.json())
      .toPromise();
  }

  /**
   * Create or update a user.
   *
   * A null 'plainPassword' field means no-change.
   */
  save(user:any):Promise<any> {
    if (user.plainPassword === null) {
      delete user['plainPassword ']
    }

    delete user['_links'];

    let body = JSON.stringify(user);
    if (user.id) {
      return this.http.patch(`/users/${user.id}`, body).toPromise();
    } else {
      return this.http.post(`/users`, body).toPromise();
    }
  }

  /**
   * Delete a user.
   */
  remove(id:number):Promise<any> {
    return this.http
      .delete(`/users/${id}`)
      .toPromise();
  }

}

/**
 * Domain class representing a User resource.
 */
export class User extends UserMixin {
  id: any;
  next: any;
  roles:[any];
  plainPassword:string;
  active: boolean;
}
