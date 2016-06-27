import {Injectable} from '@angular/core'
import {HttpClient} from "../services/httpclient.service";
import {RequestOptionsArgs, URLSearchParams} from '@angular/http'
import * as moment  from 'moment';
import {SearchOpts} from "./searchopts.ts";
import {Observable} from "rxjs/Observable";


type Moment = moment.Moment;

type CaloriesPerDayResult = [{date:string, calories:number}];

@Injectable()
export class Meals {

  constructor(private http:HttpClient) {

  }

  /**
   * Return a Meal resource, optionally applying a projection
   */
  find(id:number, projection:string=undefined):Promise<any> {
    let search = new SearchOpts();
    search.projection = projection;
    return this.http
      .get(`/meals/${id}`, {search: search.toParams()})
      .map((res) => res.json())
      .toPromise();
  }

  /**
   * List Meal resources.
   */
  list(search?: SearchOpts):Promise<any> {
    search = search || new SearchOpts();
    return this.http
      .get('/meals', {search: search.toParams()})
      .map((res) => res.json())
      .toPromise();
  }

  /**
   * Create or update a meal.
   */
  save(meal:any):Promise<any> {
    delete meal['_links'];

    let body = JSON.stringify(meal);
    if (meal.id) {
      return this.http.patch(`/meals/${meal.id}`, body).toPromise();
    } else {
      return this.http.post(`/meals`, body).toPromise();
    }
  }

  /**
   * Delete a resource.
   */
  remove(id:number):Promise<any> {
    return this.http
      .delete(`/meals/${id}`)
      .toPromise();
  }

  /**
   *  Report the sum of calories logged in an interval grouped by day.
   */
  getCaloriesPerDay(_start:Moment, _end:Moment):Promise<CaloriesPerDayResult> {
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

  /**
   * Count number of logged meals (all if ADMIN, for user if USER).
   */
  getCount() {
    return this.http
      .get('/reports/mealCount')
      .map((res) => res.json())
      .toPromise();
  }
}
