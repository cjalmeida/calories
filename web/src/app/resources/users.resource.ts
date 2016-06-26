import {Injectable} from '@angular/core'
import {HttpClient} from "../services/httpclient.service";
import {RequestOptionsArgs, URLSearchParams} from '@angular/http'
import * as moment  from 'moment';
import {extractId} from './utils';
import {SearchOpts} from "./searchopts.ts";
import {User as UserMixin} from "../services/user.mixin.ts";
type Moment = moment.Moment;

@Injectable()
export class Users {

  constructor(private http:HttpClient) {

  }

  getCount():Promise<any> {
    return this.http
      .get('/reports/userCount')
      .map((res) => res.json())
      .toPromise();
  }

  find(id:number):Promise<any> {
    return this.http
      .get(`/users/${id}`)
      .map((res) => {
        let data = res.json();
        data.id = extractId(data);
        return this.http.get(`/api/users/${id}/roles`)
          .map((resRoles) => {
            data.roles = resRoles.json()._embedded.roles;
            return data;
          })
          .toPromise();
      })
      .toPromise();
  }

  list(search?: SearchOpts):Promise<any> {
    search = search || new SearchOpts();
    return this.http
      .get('/users', {search: search.toParams()})
      .map((res) => res.json())
      .toPromise();
  }

  save(user:any):Promise<any> {
    if (user.password === null) {
      delete user['password']
    }

    delete user['_links'];

    let body = JSON.stringify(user);
    if (user.id) {
      return this.http.patch(`/users/${user.id}`, body).toPromise();
    } else {
      return this.http.post(`/users`, body).toPromise();
    }
  }

  remove(id:number):Promise<any> {
    return this.http
      .delete(`/users/${id}`)
      .toPromise();
  }

}

export class User extends UserMixin {
  id: any;
  next: any;
  roles:[any];
  password:string;
}
