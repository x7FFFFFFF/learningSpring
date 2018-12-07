import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Credentials} from './Credentials';
import {MessageService} from './message.service';
import {PartialObserver} from 'rxjs';
import {ErrorItem} from './ErrorItem';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  authenticated = false;

  constructor(private http: HttpClient, private msgService: MessageService) {
  }


  authenticate(credentials: Credentials, callback) {

    /*    const observer: PartialObserver<any> = {
          next(value) {
            console.log('next: ' + value);
          },

          error(value) { // We actually could just remove this method,
            console.log('error: ' + value);
          },        // since we do not really care about errors right now.
          complete() {
            console.log('complete: ' + this.sum);
          }
        };*/
    // this.http.post('login', credentials).subscribe(observer);

    this.http.post('login', credentials).subscribe(response => {
        const errorItems = 'errorItems';
        const errorItemsValue = response.hasOwnProperty(errorItems) ? response[errorItems] : undefined;
        if (errorItemsValue !== undefined && errorItemsValue.length > 0) {
          this.msgService.addErrors(errorItemsValue as ErrorItem[]);
          this.authenticated = false;
          return;
        }


        this.authenticated = response['status'] === 204;
        if (!this.authenticated) {
          this.msgService.add('Authentification error!');
          return;
        }

        return callback && callback();
      },
      err => {
        console.log('error: ' + err);
        this.authenticated = false;
        if (err instanceof HttpErrorResponse) {
          this.msgService.add(err.message);
        }

      }
    );

  }

}
