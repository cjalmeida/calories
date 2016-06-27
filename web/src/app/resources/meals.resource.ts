import {Injectable} from '@angular/core'
import {HttpClient} from "../services/httpclient.service";
import {RequestOptionsArgs, URLSearchParams} from '@angular/http'
import * as moment  from 'moment';
import {SearchOpts} from "./searchopts.ts";
import {Observable} from "rxjs/Observable";


type Moment = moment.Moment;

@Injectable()
export class Meals {

  constructor(private http:HttpClient) {

  }

  find(id:number, projection:string=undefined):Promise<any> {
    let search = new SearchOpts();
    search.projection = projection;
    return this.http
      .get(`/meals/${id}`, {search: search.toParams()})
      .map((res) => res.json())
      .toPromise();
  }

  list(search?: SearchOpts):Promise<any> {
    search = search || new SearchOpts();
    return this.http
      .get('/meals', {search: search.toParams()})
      .map((res) => res.json())
      .toPromise();
  }

  save(meal:any):Promise<any> {
    delete meal['_links'];

    let body = JSON.stringify(meal);
    if (meal.id) {
      return this.http.patch(`/meals/${meal.id}`, body).toPromise();
    } else {
      return this.http.post(`/meals`, body).toPromise();
    }
  }

  remove(id:number):Promise<any> {
    return this.http
      .delete(`/meals/${id}`)
      .toPromise();
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
