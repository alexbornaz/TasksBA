import {Injectable} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import {Observable} from "rxjs";
import {Task} from "../interfaces/Task";
import {HttpClient} from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class TaskService {
  apiUrl: string;

  constructor(private authService: AuthenticationService, private http: HttpClient) {
    this.apiUrl = "http://localhost:8080/api/tasks"
  }

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/all`, this.authService.httpOptions)
  }

  getTask(taskId: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${taskId}`, this.authService.httpOptions)
  }

  editTask(task: Task): Observable<any> {
    return this.http.put(`${this.apiUrl}/edit`, task, this.authService.httpOptions)
  }
}
