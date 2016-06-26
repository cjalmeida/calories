import {URLSearchParams} from '@angular/http';

export class SearchOpts {

	constructor(public page:number = 0, public size:number = 20, public projection:string = undefined){

	}

	toParams(): URLSearchParams {
		let p = new URLSearchParams();
		for (let k in this) {
      if (!_.isFunction(this[k]))
			  p.set(k, this[k]);
		}
		return p;
	}
}
