import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Status} from "../../Status";
import {Observable} from "rxjs";
import {UserDTO} from "../../interfaces/UserDTO";
import {UserService} from "../../services/user.service";
import {TaskService} from "../../services/task.service";
import {Task} from "../../interfaces/Task";
import {Location} from "@angular/common";

@Component({
  selector: 'app-new-task',
  templateUrl: './new-task.component.html',
  styleUrls: ['./new-task.component.css']
})
export class NewTaskComponent implements OnInit {
  newTaskForm: FormGroup;
  statuses = Object.values(Status)
  users?: Observable<UserDTO[]>;
  serverMessage: string
  showServerMessage: boolean

  constructor(public activeModal: NgbActiveModal, private userService: UserService, private taskService: TaskService) {
    this.serverMessage = "";
    this.showServerMessage = false;
    this.newTaskForm = new FormGroup({
      subject: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      dueDate: new FormControl('', Validators.required),
      status: new FormControl('', Validators.required),
      assignedTo: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
    this.users = this.userService.getUsernames();

  }

  onSubmit() {
    const newTask: Task = {
      subject: this.newTaskForm.get('subject')!.value,
      dueDate: this.newTaskForm.get('dueDate')!.value,
      status: this.newTaskForm.get('status')!.value,
      assignedTo: this.newTaskForm.get('assignedTo')!.value

    }
    this.taskService.createTask(newTask).subscribe(response => {
        this.serverMessage = response.message
        this.showServerMessage = true;
        this.newTaskForm.reset()
        this.activeModal.close();
        window.location.reload()
      }
    )
  }
}
