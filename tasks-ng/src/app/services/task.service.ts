import {Injectable} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import {catchError, map, Observable, Subject, throwError} from "rxjs";
import {Task} from "../interfaces/Task";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {SearchReq} from "../interfaces/SearchReq";
import {Pagination} from "../interfaces/Pagination";


@Injectable({
  providedIn: 'root'
})
export class TaskService {
  apiUrl: string;

  private refreshSubject = new Subject<void>();
  refreshComponent$ = this.refreshSubject.asObservable()

  triggerRefresh() {
    this.refreshSubject.next();
  }

  constructor(private authService: AuthenticationService, private http: HttpClient) {
    this.apiUrl = "http://localhost:8080/api/tasks"
  }

  getAllTasks(page: number): Observable<Pagination<Task>> {
    return this.http.get<Pagination<Task>>(`${this.apiUrl}?page=${page}`, {
      ...this.authService.httpOptions,
      observe: 'response'
    }).pipe(
      map((response: HttpResponse<any>) => {
        const page: Pagination<Task> = {
          items: response.body.content,
          currentPage: response.body.pageable.pageNumber + 1,
          totalPages: response.body.totalPages
        }
        return page;
      }),
      catchError((error: HttpErrorResponse) => {
        return throwError(error.error.message)
      })
    )
  }

  getTask(taskId: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${taskId}`, {...this.authService.httpOptions, observe: 'response'})
      .pipe(
        map((response: HttpResponse<any>) => {
          return response.body
        }),
        catchError((error: HttpErrorResponse) => {
          return throwError(error.error.message)
        })
      )
  }

  editTask(task: Task): Observable<string> {
    return this.http.put(`${this.apiUrl}/edit`, task, {...this.authService.httpOptions, observe: 'response'})
      .pipe(
        map((response: HttpResponse<any>) => {
          return response.body.message
        }),
        catchError((error: HttpErrorResponse) => {
          return throwError(error.error.message)
        })
      )
  }

  // getTasksBySearchTerms(searchTerms: SearchReq): Observable<Task[]> {
  //   return this.http.post<Task[]>(`${this.apiUrl}/search`, searchTerms, this.authService.httpOptions)
  // }

  getTasksBySearchTerms(searchTerms: SearchReq, page: number): Observable<Pagination<Task>> {
    return this.http.post<Pagination<Task>>(`${this.apiUrl}/search?page=${page}`, searchTerms, {
      ...this.authService.httpOptions,
      observe: "response"
    })
      .pipe(
        map((response: HttpResponse<any>) => {
          const page: Pagination<Task> = {
            items: response.body.content,
            currentPage: response.body.pageable.pageNumber + 1,
            totalPages: response.body.totalPages
          }
          return page;
        }),
        catchError((error: HttpErrorResponse) => {
          return throwError(error.error.message);
        })
      );
  }

  createTask(task: Task): Observable<string> {
    return this.http.post(`${this.apiUrl}/add`, task, {...this.authService.httpOptions, observe: 'response'})
      .pipe(
        map((response: HttpResponse<any>) => {
          return response.body.message
        }),
        catchError((error: HttpErrorResponse) => {
          return throwError(error.error.message)
        })
      )
  }

  deleteTask(taskId: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${taskId}`, {...this.authService.httpOptions, observe: 'response'})
      .pipe(
        map((response: HttpResponse<any>) => {
          return response.body.message
        }),
        catchError((error: HttpErrorResponse) => {
          return throwError(error.error.message)
        })
      );
  }
}
