import {Injectable, Inject} from '@angular/core'
import {Http, ConnectionBackend, RequestOptions, Headers, RequestOptionsArgs, Response} from '@angular/http'
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class HttpClient {

  private headers:any = {'Content-Type': 'application/json'};
  private base: string;

  unauthorizedError = new Subject<any>();

  constructor(private http:Http, @Inject('config') private config:any) {
    this.base = config['API_BASE'].replace(/\/+$/, '');
  }

  setHeader(key:string, val:string) {
    if (!val) {
      delete this.headers[key];
    } else {
      this.headers[key] = val;
    }
  }

  private mergeHeaders(options:RequestOptionsArgs) {
    if (!options) return;

    options.headers = options.headers || new Headers();
    for (let k in this.headers) {
      options.headers.set(k, this.headers[k]);
    }
  }

  request(path:string, options:RequestOptionsArgs):Observable<Response> {
    this.mergeHeaders(options);

    let _call = this.http.request(this.base + path, options);

    _call.subscribe(null, (err) => {
      if (err.status == 401) {
        // Notify the 401 error
        this.unauthorizedError.next(err);
      }
    });

    return _call;
  }

  get(path:string, options?:RequestOptionsArgs):Observable<Response> {
    options = options || {};
    options.method = 'GET';
    return this.request(path, options);
  }

  post(path:string, body:string, options?:RequestOptionsArgs):Observable<Response> {
    options = options || {};
    options.method = 'POST';
    options.body = body;
    return this.request(path, options);
  }

  patch(path:string, body:string, options?:RequestOptionsArgs):Observable<Response> {
    options = options || {};
    options.method = 'PATCH';
    options.body = body;
    return this.request(path, options);
  }

  delete(path:string, options?:RequestOptionsArgs):Observable<Response> {
    options = options || {};
    options.method = 'DELETE';
    return this.request(path, options);
  }


}
