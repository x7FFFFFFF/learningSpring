import {Role} from './Role';
import {DataSource} from '@angular/cdk/typings/collections';
import {CollectionViewer} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {RolesServiceService} from './roles-service.service';
import {catchError, finalize} from 'rxjs/operators';
import {Result} from './Result';

export class RolesDataSource implements DataSource<Role> {

  private roleSubject = new BehaviorSubject<Role[]>([]);

  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public count$ = this.countSubject.asObservable();

  constructor(private coursesService: RolesServiceService) {
  }

  loadRoles(
    filter: string,
    sortDirection: string,
    pageIndex: number,
    pageSize: number) {

    this.loadingSubject.next(true);

    this.coursesService.findRoles(filter, sortDirection,
      pageIndex, pageSize).pipe(
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(res => {
        this.roleSubject.next((res as Result).content);
        this.countSubject.next((res as Result).totalElements);
      });

  }


  connect(collectionViewer: CollectionViewer): Observable<Role[] | ReadonlyArray<Role>> {
    console.log('Connecting data source');
    return this.roleSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.roleSubject.complete();
    this.loadingSubject.complete();
    this.countSubject.complete();
  }


}
