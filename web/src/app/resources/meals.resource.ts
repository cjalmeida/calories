import {Injectable} from '@angular/core'
import {HttpClient} from "../services/httpclient.service";
import {RequestOptionsArgs, URLSearchParams} from '@angular/http'
import * as moment  from 'moment';

type Moment = moment.Moment;

@Injectable()
export class Meals {

  constructor(private http:HttpClient) {

  }

  getCaloriesPerDay(_start:Moment, _end:Moment):Promise<any> {
    const mealDateStart = _start.format('YYYY-MM-DD');
    const mealDateEnd = _end.format('YYYY-MM-DD');
    const search = new URLSearchParams();
    search.set("mealDateStart", mealDateStart);
    search.set("mealDateEnd", mealDateEnd);
    const options: RequestOptionsArgs = { search };

    return this.http
      .get('/reports/caloriesPerDay', options)
      .map((res) => res.json())
      .toPromise();
  }

  getCount() {
    return this.http
      .get('/reports/mealCount')
      .map((res) => res.json())
      .toPromise();
  }
}
