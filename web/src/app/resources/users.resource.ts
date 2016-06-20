import {Injectable} from '@angular/core'
import {HttpClient} from "../services/httpclient.service";
import {RequestOptionsArgs, URLSearchParams} from '@angular/http'
import * as moment  from 'moment';
import {extractId} from './utils';

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
        return this.http.get(`/users/${id}/roles`)
          .map((resRoles) => {
            data.roles = resRoles.json()._embedded.roles;
            return data;
          })
          .toPromise();
      })
      .toPromise();
  }

  list(page:number = null):Promise<any> {
    const search = new URLSearchParams();
    search.set("projection", "simple");
    if (page !== null) {
      search.set("page", page.toString());
    }
    return this.http
      .get('/users', {search: search})
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
}
