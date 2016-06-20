import {Injectable} from '@angular/core'
import {HttpClient} from "../services/httpclient.service";
import * as moment  from 'moment';

type Moment = moment.Moment;

@Injectable()
export class Roles {

  constructor(private http:HttpClient) {

  }

  list():Promise<any> {
    return this.http
      .get('/roles')
      .map((res) => res.json())
      .toPromise();
  }
}
