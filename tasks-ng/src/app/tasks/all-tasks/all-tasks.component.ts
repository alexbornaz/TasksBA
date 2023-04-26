import {Component, OnInit} from '@angular/core';
import {TaskService} from "../../services/task.service";
import {map, Observable, of} from "rxjs";
import {Task} from 'src/app/interfaces/Task';

@Component({
  selector: 'app-all-tasks',
  templateUrl: './all-tasks.component.html',
  styleUrls: ['./all-tasks.component.css']
})
export class AllTasksComponent implements OnInit{
  tasks: Observable<Task[]> = of([])

  constructor(private taskService: TaskService) {
  }
  ngOnInit():void{
    this.tasks=this.getAllTasks();
  }

  getAllTasks(): Observable<Task[]> {
    return this.taskService.getAllTasks().pipe(
      map(tasks => tasks.sort((a, b) => new Date(b.dueDate).getTime() - new Date(a.dueDate).getTime()))
    );
  }
}
