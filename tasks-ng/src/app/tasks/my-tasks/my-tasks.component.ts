import {Component} from '@angular/core';
import {UserService} from "../../services/user.service";
import {Task} from "../../interfaces/Task";
import {map, Observable, of} from "rxjs";

@Component({
  selector: 'app-my-tasks',
  templateUrl: './my-tasks.component.html',
  styleUrls: ['./my-tasks.component.css']
})
export class MyTasksComponent {
  private username?: string
  tasks$: Observable<Task[]>=of([])

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.retrieveUsername();
    this.tasks$ = this.getTasks();
  }

  retrieveUsername(): void {
    const username = this.userService.getUsername();
    if (typeof username == "string") {
      this.username = username;
    }
  }

  getTasks(): Observable<Task[]> {
    return this.userService.getTasks(this.username).pipe(
      map(tasks => tasks.sort((a, b) => new Date(b.dueDate).getTime() - new Date(a.dueDate).getTime()))
    );
  }
}
