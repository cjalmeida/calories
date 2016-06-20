import {Component} from '@angular/core';
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from '@angular/common';
import {CAROUSEL_DIRECTIVES, DROPDOWN_DIRECTIVES, AlertComponent} from 'ng2-bootstrap/ng2-bootstrap';

@Component({
	moduleId: module.id,
	selector: 'timeline-cmp',
	template: require('./timeline.html'),
	styles: [require('./timeline.scss')],
})
class TimelineComponent { }

@Component({
	moduleId: module.id,
	selector: 'chat-cmp',
	template: require('./chat.html'),
	directives: [CORE_DIRECTIVES, DROPDOWN_DIRECTIVES]
})
class ChatComponent {}

@Component({
	moduleId: module.id,
	selector: 'notifications-cmp',
	template: require('./notifications.html'),
	styles: [],
})
class NotificationComponent { }


@Component({
	moduleId: module.id,
	selector: 'home-cmp',
	template: require('./home.html'),
	styles: [require('./home.scss')],
	directives: [
		AlertComponent,
		TimelineComponent,
		ChatComponent,
		NotificationComponent,
		CAROUSEL_DIRECTIVES,
		CORE_DIRECTIVES,
		FORM_DIRECTIVES ]
})

export class Home2Component {

	/* Carousel Variable */
	myInterval: number = 5000;
	index: number = 0;
	slides: Array<any> = [];
	imgUrl: Array<any> = [
		`img/slider1.jpg`,
		`img/slider2.jpg`,
		`img/slider3.jpg`,
		`img/slider0.jpg`
	];
	/* END */
	/* Alert component */
	public alerts:Array<Object> = [
	   {
	     type: 'danger',
	     msg: 'Oh snap! Change a few things up and try submitting again.'
	   },
	   {
	     type: 'success',
	     msg: 'Well done! You successfully read this important alert message.',
	     closable: true
	   }
	 ];

	 public closeAlert(i:number):void {
	   this.alerts.splice(i, 1);
	 }
	/* END*/

	constructor() {
		for (let i = 0; i < 4; i++) {
			this.addSlide();
		}
	}

	/* Carousel */
	addSlide() {
		let i = this.slides.length;
		this.slides.push({
			image: this.imgUrl[i],
			text: `${['Dummy ', 'Dummy ', 'Dummy ', 'Dummy '][this.slides.length % 4]}
      			${['text 0', 'text 1', 'text 2', 'text 3'][this.slides.length % 4]}`
		});
	}
	/* END */
}
