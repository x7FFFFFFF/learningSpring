import {Component} from '@angular/core';
import {AppService} from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-admin';


  constructor(private appSrv: AppService) {
  }


  authenticated(): boolean {
    return this.appSrv.authenticated;
  }


  logout() {
    this.appSrv.logout();
  }
}
