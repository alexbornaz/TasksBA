import {Component, OnInit} from '@angular/core';
import {TaskService} from "../../services/task.service";
import {Observable, of, Subscription} from "rxjs";
import {Task} from 'src/app/interfaces/Task';
import {FormGroup} from "@angular/forms";
import {SearchReq} from "../../interfaces/SearchReq";


@Component({
  selector: 'app-all-tasks',
  templateUrl: './all-tasks.component.html',
  styleUrls: ['./all-tasks.component.css']
})
export class AllTasksComponent implements OnInit {
  tasks: Observable<Task[]> = of([])
  private refreshSub?: Subscription;
  protected currentPage: number = 1
  protected totalPages: number = 0
  searchParams?: SearchReq;

  constructor(private taskService: TaskService) {
  }

  ngOnInit(): void {
    this.refreshSub = this.taskService.refreshComponent$.subscribe(() => {
      this.getAllTasks(this.currentPage);
    })
    this.getAllTasks(this.currentPage);
  }


  getAllTasks(page: number) {
    this.taskService.getAllTasks(page).subscribe((tasks) => {
      this.tasks = of(tasks.items);
      this.currentPage = tasks.currentPage;
      this.totalPages = tasks.totalPages;
    });
  }


  onSearch(searchParams: FormGroup) {
    this.searchParams = {
      subject: searchParams.get('subject')!.value,
      assignedTo: searchParams.get('assignedTo')!.value,
      dueDate: searchParams.get('dueDate')!.value,
      status: searchParams.get('status')!.value
    }
    const initialPage = 1;
    this.getTasksByPage(initialPage)
  }

  getTasksByPage(page: number): void {
    if (this.searchParams) {
      this.taskService.getTasksBySearchTerms(this.searchParams, page).subscribe((pagination) => {
        this.tasks = of(pagination.items);
        this.currentPage = pagination.currentPage;
        this.totalPages = pagination.totalPages;
      });
    } else {
      this.taskService.getAllTasks(page).subscribe((pagination) => {
        this.tasks = of(pagination.items);
        this.currentPage = pagination.currentPage;
        this.totalPages = pagination.totalPages;
      });
    }
  }

  handleAskedPage(page: number): void {
    this.currentPage = page;
    this.getTasksByPage(this.currentPage)
  }
}
