import {Component, OnInit} from '@angular/core';
import {Task} from "../../interfaces/Task";
import {ActivatedRoute, Router} from "@angular/router";
import {TaskService} from "../../services/task.service";
import {Observable} from "rxjs";
import {Status} from "../../Status";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {UserDTO} from "../../interfaces/UserDTO";
import {ToastrService} from "ngx-toastr";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {GenericModalComponent} from "../../generic-modal/generic-modal.component";

@Component({
  selector: 'app-task-detail',
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.css']
})
export class TaskDetailComponent implements OnInit {
  task?: Task;
  editableMode: boolean = false;
  taskForm: FormGroup;
  statuses = Object.values(Status)
  originalTask?: Task
  users?: Observable<UserDTO[]>;
  taskId?: number

  constructor(private route: ActivatedRoute, private taskService: TaskService, private userService: UserService,
              private toastr: ToastrService, private modalService: NgbModal, private router: Router) {
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
      this.taskId = params['id']
      this.getTask(taskId);
    })
  }

  private getTask(id: number): void {
    this.taskService.getTask(id).subscribe(
      {
        next: (task) => {
          this.task = task;
          this.originalTask = task;
          this.taskForm.setValue({
            subject: task.subject,
            dueDate: task.dueDate,
            status: task.status,
            assignedTo: task.assignedTo.username
          })
        },
        error: (error) => {
          this.router.navigate([''])
          this.toastr.error(error, "error", {progressBar: true})
        }
      }
    )
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
    this.taskService.editTask(newTask).subscribe(
      {
        next: (msg) => {
          this.originalTask = newTask;
          this.editableMode = false;
          this.taskForm.disable()
          this.toastr.success(msg, "success", {progressBar: true, closeButton: true})
        },
        error:(err)=>{
              this.toastr.error(err, "error", {progressBar: true, closeButton: true})
        }
      }
    );

  }

  deleteTask() {
    const deleteModal = this.modalService.open(GenericModalComponent);
    deleteModal.componentInstance.title = "Delete Task"
    deleteModal.componentInstance.content = "Are you sure you want to delete this task?";
    deleteModal.result.then(() => {
      this.taskService.deleteTask(this.taskId!).subscribe(
        {
          next: (msg) => {
            this.router.navigate([''])
            this.toastr.success(msg, "success", {progressBar: true, closeButton: true})
          }
          , error: (err) => {
            this.toastr.error(err, "success", {progressBar: true, closeButton: true})
          }
        }
      )
    }, (reason) => {

    })
  }
}
