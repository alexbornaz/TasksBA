import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {Task} from "../../interfaces/Task";
import {catchError, Observable, of, Subscription} from "rxjs";
import {TaskService} from "../../services/task.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-my-tasks',
  templateUrl: './my-tasks.component.html',
  styleUrls: ['./my-tasks.component.css']
})
export class MyTasksComponent implements OnInit {
  private username?: string
  tasks$?: Observable<Task[]> = of([])
  private refreshSub?: Subscription

  constructor(private userService: UserService, private taskService: TaskService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    this.refreshSub = this.taskService.refreshComponent$.subscribe(() => {
      this.tasks$ = this.getTasks()
    })
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
    return this.userService.getAssignedTasks(this.username).pipe(
      catchError((err) => {
        this.toastr.error(err, "error", {progressBar: true})
        return of([])
      })
    );
  }
}
