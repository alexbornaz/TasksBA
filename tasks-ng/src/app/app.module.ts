import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {UserAuthenticationModule} from "./user-authentication/user-authentication.module";
import {TasksModule} from "./tasks/tasks.module";
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {JwtModule, JwtModuleOptions} from "@auth0/angular-jwt";
import {ToastrModule} from "ngx-toastr";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {GenericModalComponent} from './generic-modal/generic-modal.component';

const JWT_Module_Options: JwtModuleOptions = {
  config: {
    tokenGetter: () => {
      return localStorage.getItem("token")
    },
  }
};

@NgModule({
  declarations: [
    AppComponent,
    GenericModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    UserAuthenticationModule,
    TasksModule,
    NgbModule,
    JwtModule.forRoot(JWT_Module_Options),
    ToastrModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
