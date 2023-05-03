import {Component, OnInit} from '@angular/core';
import {Task} from "../../interfaces/Task";
import {ActivatedRoute} from "@angular/router";
import {TaskService} from "../../services/task.service";
import {Observable, of} from "rxjs";
import {Status} from "../../Status";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {UserDTO} from "../../interfaces/UserDTO";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-task-detail',
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.css']
})
export class TaskDetailComponent implements OnInit {
  task?: Observable<Task>;
  editableMode: boolean = false;
  taskForm: FormGroup;
  statuses = Object.values(Status)
  originalTask?: Task
  users?:Observable<UserDTO[]>;

  constructor(private route: ActivatedRoute, private taskService: TaskService, private userService: UserService,
              private toastr:ToastrService) {
    this.taskForm = new FormGroup({
      subject: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      dueDate: new FormControl('', Validators.required),
      status: new FormControl('', Validators.required),
      assignedTo: new FormControl('')
    });
    this.taskForm.disable()
  }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      this.users = this.getUsernames();
      const taskId = params['id']
      this.task = this.getTask(taskId)
      this.task.subscribe(task => {
        this.originalTask = task;
        this.taskForm.setValue({
          subject: task.subject,
          dueDate: task.dueDate,
          status: task.status,
          assignedTo: task.assignedTo.username
        })
      })
    })
  }

  private getTask(id: number): Observable<Task> {
    return this.taskService.getTask(id);
  }

  private getUsernames(): Observable<UserDTO[]> {
    return this.userService.getUsernames()
  }

  onEdit(): void {
    this.editableMode = true;
    this.taskForm.enable()
  }

  onCancel(): void {
    this.taskForm.setValue({
      subject: this.originalTask?.subject,
      dueDate: this.originalTask?.dueDate,
      status: this.originalTask?.status,
      assignedTo: this.originalTask?.assignedTo.username
    });
  }

  onSave() {
    const newTask: Task = {
      id: this.originalTask?.id || 0,
      subject: this.taskForm.get('subject')?.value || '',
      dueDate: this.taskForm.get('dueDate')?.value || '',
      status: this.taskForm.get('status')?.value || '',
      assignedTo: this.taskForm.get('assignedTo')?.value || ''
    };
    this.taskService.editTask(newTask).subscribe(response => {
      this.originalTask = newTask;
      this.editableMode = false;
      this.taskForm.disable()
      if (response.success){
        this.toastr.success(response.message,"success",{progressBar:true,closeButton:true})
      }else {
        this.toastr.error(response.message,"error",{progressBar:true,closeButton:true})
      }
    });

  }
}
