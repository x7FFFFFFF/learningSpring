import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {AppService} from './app.service';
import {RequestInterceptor} from './RequestInterceptor';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {MessageComponent} from './message/message.component';
import {MessageService} from './message.service';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RolesComponent } from './roles/roles.component';
import { AccountsComponent } from './accounts/accounts.component';
import { MatInputModule, MatPaginatorModule, MatProgressSpinnerModule,
  MatSortModule, MatTableModule } from '@angular/material';
import {RolesServiceService} from './roles/roles-service.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'login'},
  {path: 'admin/login', redirectTo: 'login'},
  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'roles', component: RolesComponent},
  {path: 'accounts', component: AccountsComponent}


];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MessageComponent,
    DashboardComponent,
    RolesComponent,
    AccountsComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    BrowserAnimationsModule
  ],
  /* exports:[
     MessageComponent
   ],*/
  providers: [AppService, MessageService, RolesServiceService,
    {provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
