import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterComponent} from "./user-authentication/register/register.component";
import {LoginComponent} from "./user-authentication/login/login.component";
import {AuthGuard} from "./auth.guard";
import {AppComponent} from "./app.component";
import {LoginRegisterGuard} from "./login-register.guard";

const routes: Routes = [
  {path: '', component: AppComponent, canActivate: [AuthGuard]},
  {path: 'register', component: RegisterComponent, canActivate:[LoginRegisterGuard]},
  {path: 'login', component: LoginComponent,canActivate:[LoginRegisterGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
