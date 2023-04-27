import {Component, OnInit} from '@angular/core';
import {TaskService} from "../../services/task.service";
import {Observable, of} from "rxjs";
import {Task} from 'src/app/interfaces/Task';
import {FormBuilder, FormGroup} from "@angular/forms";
import {SearchReq} from "../../interfaces/SearchReq";

@Component({
  selector: 'app-all-tasks',
  templateUrl: './all-tasks.component.html',
  styleUrls: ['./all-tasks.component.css']
})
export class AllTasksComponent implements OnInit {
  tasks: Observable<Task[]> = of([])
  searchForm: FormGroup;

  constructor(private taskService: TaskService, private formBuilder: FormBuilder) {
    this.searchForm = this.formBuilder.group({
      assignedTo: '',
      dueDate: '',
      status: '',
      subject: ''
    });
  }

  ngOnInit(): void {
    this.tasks = this.getAllTasks();
  }

  getAllTasks(): Observable<Task[]> {
    return this.taskService.getAllTasks()
  }

  onSearch(searchParams: FormGroup) {
    const searchTerms: SearchReq = {
      subject: searchParams.get('subject')!.value,
      assignedTo: searchParams.get('assignedTo')!.value,
      dueDate: searchParams.get('dueDate')!.value,
      status: searchParams.get('status')!.value
    }
    this.tasks = this.taskService.getTasksBySearchTerms(searchTerms)
  }
}
