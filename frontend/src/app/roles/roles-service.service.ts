import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Role} from './Role';
import {Result} from './Result';

@Injectable({
  providedIn: 'root'
})
export class RolesServiceService {

  constructor(private http: HttpClient) {
  }

  findRoles(
    filter = '', sortOrder = 'asc',
    pageNumber = 0, pageSize = 20): Observable<Result> {

    return this.http.get('/roles', {
      params: new HttpParams()
        .set('filter', filter)
        .set('sortOrder', sortOrder)
        .set('pageNumber', pageNumber.toString())
        .set('pageSize', pageSize.toString())
    }).pipe(
      map(result => {
        const res = result['result'];

        return new Result(res['number'], res['totalElements'], res['totalPages'], res['content']);

      })
    );
  }
}
