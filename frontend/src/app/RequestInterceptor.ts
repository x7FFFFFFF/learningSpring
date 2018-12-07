import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {environment} from '../environments/environment';
import {catchError, map} from 'rxjs/operators';
import {MessageService} from './message.service';


@Injectable()
export class RequestInterceptor implements HttpInterceptor {

  constructor(private msgService: MessageService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('req.headers =', req.headers, ';');
    req = req.clone({
      url: environment.apiUrl + req.url
    });
    return next.handle(req).pipe(
      map((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          console.log('HttpResponse::event =', event, ';');
        } else {
          console.log('event =', event, ';');
        }
        return event;
      }),
      catchError((err: any, caught) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 403) {
            console.log('err.error =', err.error, ';');
          }
          return throwError(err);
        }
      })
    );
  }


  /* intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
     req = req.clone({
       url: environment.apiUrl + req.url
     });
     return next.handle(req).pipe( catchError(err => this.handleAuthError(err)));
   }


   private handleAuthError(err: HttpErrorResponse): Observable<any> {
     const message = 'err: ' + err.message;
     this.msgService.add(message);
     return of(message);
   }*/
}
