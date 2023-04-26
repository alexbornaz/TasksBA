import {Component, OnInit} from '@angular/core';
import {Task} from "../../interfaces/Task";
import {ActivatedRoute} from "@angular/router";
import {TaskService} from "../../services/task.service";
import {Observable} from "rxjs";
import {Status} from "../../Status";
import {FormControl, FormGroup, Validators} from "@angular/forms";

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

  constructor(private route: ActivatedRoute, private taskService: TaskService) {
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
      console.log(response)
      console.log('New Task:', newTask);

      this.originalTask = newTask;
      this.editableMode = false;
      this.taskForm.disable()
    });

  }
}
