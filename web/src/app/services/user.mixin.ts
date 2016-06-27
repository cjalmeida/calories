import * as _ from 'lodash'

// A minimum user class for authentication purposes.
export class User {
  fullName: String;
  email: String;
  roles: [String];

  constructor(json:Object = null) {
    if (json) {
      _.assign(this, json);
    }
  }

  hasRole(...roles:string[]): boolean {
    for (let role of roles) {
      if (this.roles.indexOf(role) > -1) return true;
    }
    return false;
  }
}
