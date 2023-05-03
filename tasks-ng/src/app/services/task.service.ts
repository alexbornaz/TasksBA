import {Injectable} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import {Observable, Subject} from "rxjs";
import {Task} from "../interfaces/Task";
import {HttpClient} from "@angular/common/http";
import {SearchReq} from "../interfaces/SearchReq";


@Injectable({
  providedIn: 'root'
})
export class TaskService {
  apiUrl: string;

  private refreshSubject = new Subject<void>();
  refreshComponent$ = this.refreshSubject.asObservable()

  triggerRefresh(){
    this.refreshSubject.next();
  }

  constructor(private authService: AuthenticationService, private http: HttpClient) {
    this.apiUrl = "http://localhost:8080/api/tasks"
  }

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}`, this.authService.httpOptions)
  }

  getTask(taskId: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${taskId}`, this.authService.httpOptions)
  }

  editTask(task: Task): Observable<any> {
    return this.http.put(`${this.apiUrl}/edit`, task, this.authService.httpOptions)
  }

  getTasksBySearchTerms(searchTerms: SearchReq): Observable<Task[]> {
    return this.http.post<Task[]>(`${this.apiUrl}/search`, searchTerms, this.authService.httpOptions)
  }

  createTask(task: Task): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`,task,this.authService.httpOptions)
  }
}
