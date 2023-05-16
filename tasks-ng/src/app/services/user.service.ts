import {Injectable} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import jwtDecode from "jwt-decode";
import {Task} from "../interfaces/Task";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {catchError, map, Observable, throwError} from "rxjs";
import {UserDTO} from "../interfaces/UserDTO";
import {Pagination} from "../interfaces/Pagination";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiUrl: string = "http://localhost:8080/api/users"
  decodedToken ?: any;


  constructor(private authService: AuthenticationService, private http: HttpClient) {
    const token = this.authService.getToken();
    if (typeof token === "string") {
      this.decodedToken = jwtDecode(token)
    }
  }


  getUsername(): string | null {
    if (this.decodedToken) {
      return this.decodedToken.sub;
    }
    return null;
  }

  getAssignedTasks(username: string | undefined, askedPage: number): Observable<Pagination<Task>> {
    return this.http.get<Pagination<Task>>(`${this.apiUrl}/tasks/${username}?page=${askedPage}`, {
      ...this.authService.httpOptions,
      observe: "response"
    })
      .pipe(
        map((response: HttpResponse<any>) => {
          console.log(response.body
          )
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

  getUsernames(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/usernames`, this.authService.httpOptions)
  }

}
