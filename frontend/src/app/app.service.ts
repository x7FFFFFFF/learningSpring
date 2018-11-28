import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Credentials} from './Credentials';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  authenticated = false;

  constructor(private http: HttpClient) {
  }


  authenticate(credentials: Credentials, callback) {

   /* const headers = new HttpHeaders(credentials ? {
      authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password)
    } : {});*/

    this.http.post('/login', credentials).subscribe(response => {
      this.authenticated = response['status'] === 204;
      return callback && callback();
    });

  }

}
