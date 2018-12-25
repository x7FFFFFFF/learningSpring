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
  description: String = 'Roles';

  dataSource: RolesDataSource;

  displayedColumns = ['name', 'parent', 'privileges'];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;

  rolesCount = 100;

  constructor(private rolesService: RolesServiceService) {
  }

  ngOnInit() {
    this.dataSource = new RolesDataSource(this.rolesService);
    this.dataSource.loadRoles('', 'asc', 0, 10);
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

  }

  loadRolesPage() {
    this.dataSource.loadRoles(
      this.input.nativeElement.value,
      this.sort.direction,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

}
