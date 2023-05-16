import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {Task} from "../../interfaces/Task";
import {Observable, of, Subscription} from "rxjs";
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
  protected totalPages: number = 0
  protected currentPage: number = 1;


  constructor(private userService: UserService, private taskService: TaskService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    this.refreshSub = this.taskService.refreshComponent$.subscribe(() => {
      this.currentPage = 1
      this.handleAskedPage(this.currentPage)
    })
    this.retrieveUsername();
    this.handleAskedPage(this.currentPage)
  }

  retrieveUsername(): void {
    const username = this.userService.getUsername();
    if (typeof username == "string") {
      this.username = username;
    }
  }

  handleAskedPage(asked: number) {
    this.userService.getAssignedTasks(this.username, asked).subscribe({
      next: pagination => {
        this.tasks$ = of(pagination.items)
        this.totalPages = pagination.totalPages;
        this.currentPage = pagination.currentPage;
      }, error: err => {
        this.toastr.error(err, "error", {progressBar: true})
      }
    })
  }
}
