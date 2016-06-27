import * as _ from 'lodash';

/**
 * Extract and return the id from a HAL resource
 */
export function extractId(obj):any {
  let href:string = _.get(obj, '_links.self.href', null);
  if (href === null) return null;

  let i = href.lastIndexOf('/');
  return href.substr(i+1);
}
