import {Component, DoCheck} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "./authentication.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'tasks-ng';
  isMainWindowOn = false;

  constructor(private router: Router,private authService:AuthenticationService) {
  }

  // ngDoCheck():void{
  //   let currentRoute =this.router.url;
  //   if (currentRoute == "/login" || currentRoute == "/register"){
  //       this.isMainWindowOn=false;
  //   }else {
  //     this.isMainWindowOn=true;
  //   }
  // }

}
