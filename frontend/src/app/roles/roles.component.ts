import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AppService} from '../app.service';
import {RolesDataSource} from './RolesDataSource';
import {MatPaginator, MatSort} from '@angular/material';

import {RolesServiceService} from './roles-service.service';
import {fromEvent, merge} from 'rxjs';
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.css']
})
export class RolesComponent implements OnInit, AfterViewInit {
  readonly description: String = 'Roles';

  dataSource: RolesDataSource;
  count = 0;

  readonly displayedColumns = ['name', 'parent', 'privileges'];

  readonly DELIM = ',';

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;


  constructor(private rolesService: RolesServiceService) {
  }

  ngOnInit() {
    this.dataSource = new RolesDataSource(this.rolesService);
    this.dataSource.loadRoles('', 'asc', 0, 1);
  }

  ngAfterViewInit() {

    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;

          this.loadRolesPage();
        })
      )
      .subscribe();

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadRolesPage())
      )
      .subscribe();

    this.dataSource.count$.subscribe(value => {
      this.count = value;
    });
  }

  loadRolesPage() {
    this.dataSource.loadRoles(
      this.input.nativeElement.value,
      this.sort.active + this.DELIM + this.sort.direction,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  getCount() {
    return this.count;
  }
}
