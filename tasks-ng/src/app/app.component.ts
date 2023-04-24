import {Component, DoCheck} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'tasks-ng';
  isMainWindowOn = false;

  constructor(private router: Router) {
  }

  ngDoCheck():void{
    let currentRoute =this.router.url;
    this.isMainWindowOn = !(currentRoute == "/login" || currentRoute == "/register");
  }

}
