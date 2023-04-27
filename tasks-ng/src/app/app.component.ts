import {Component, DoCheck, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "./services/authentication.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewTaskComponent} from "./tasks/new-task/new-task.component";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'tasks-ng';
  isMainWindowOn = false;


  constructor(private router: Router, private authService: AuthenticationService,private modalService: NgbModal) {
  }

  ngDoCheck(): void {
    let currentRoute = this.router.url;
    this.isMainWindowOn = !(currentRoute == "/login" || currentRoute == "/register");
  }

  handleLogout():void{
    this.authService.logout();
    location.reload();

  }
  openModal() {
    const modalRef = this.modalService.open(NewTaskComponent);
  }


}
