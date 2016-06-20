import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {CardDirective} from "../../../directives/card/components/card";
import {ChartDirective} from '../../../directives/chart/components/chart';
import {User} from "../../../services/user.mixin";
import {Meals, Users} from "../../../resources";
import {Router} from '@ngrx/router';
import * as moment from 'moment';
import * as _ from 'lodash';


const COLOR_CALORIES_LOW = '#5CB85C';
const COLOR_CALORIES_HIGH = '#D9534F';

@Component({
  moduleId: module.id,
  directives: [CardDirective, ChartDirective],
  template: require('./home.html')
})
export class HomeComponent implements OnInit {
  user:User;

  caloriesPerDay:any = null;

  userCount:number = null;
  mealCount:number = null;

  constructor(public auth:AuthService, private meals:Meals, private users: Users, private router:Router) {

  }

  go(path) {
    this.router.go(path);
  }

  ngOnInit() {
    this.auth.getCurrentUser().then((user) => {
      this.user = user;
      this.setupCaloriesPerDay();
      this.setupCounts();
    });
  }

  private setupCounts() {
    if (this.user.hasRole('ROLE_ADMIN') || this.user.hasRole('ROLE_MANAGER')) {
      this.users.getCount().then(
        (res) => this.userCount = res.userCount
      );
    }

    this.meals.getCount().then(
      (res) => this.mealCount = res.mealCount
    )
  }

  private setupCaloriesPerDay() {
    let start = moment().subtract(15, 'days');
    let end = moment();
    let userCalsPerDay = this.user['caloriesPerDay'] || 0;
    this.meals.getCaloriesPerDay(start, end).then(
      (data) => {
        let backgroundColor = _.map(data,
          (e:any) => e.calories < userCalsPerDay ? COLOR_CALORIES_LOW : COLOR_CALORIES_HIGH
        );
        let labels = _.map(data, 'date');
        let data = _.map(data, 'calories');
        this.caloriesPerDay = {
          type:'bar',
          options: {
            scaleShowVerticalLines: false,
            responsive: true,
            scales: {
              yAxes: [{
                ticks: {
                  min: 600,
                  stepSize: 200
                }
              }]
            }
          },
          data: {
            labels,
            datasets: [{
              label: 'Calories per day',
              backgroundColor,
              data,
            }]
          }
        };
      }
    )
  }

}
