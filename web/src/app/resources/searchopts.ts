import {URLSearchParams} from '@angular/http';
import * as _ from 'lodash';

/**
 * Query parameters for searching paged Spring Data REST resources.
 */
export class SearchOpts {

  constructor(public page:number = 0,
              public size:number = 20,
              public projection:string = undefined,
              public sort:string[] = []) {

  }

  /**
   * Convert to @angular/http search parameters.
   */
  toParams():URLSearchParams {
    let p = new URLSearchParams();
    for (let k in this) {
      if (k == 'sort') {
        for (let crit of this.sort) {
          p.append('sort', crit)
        }
      } else if (!_.isFunction(this[k])) {
        p.set(k, this[k]);
      }
    }
    return p;
  }
}
