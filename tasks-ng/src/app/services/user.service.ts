import {Injectable} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import jwtDecode from "jwt-decode";
import {Task} from "../interfaces/Task";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDTO} from "../interfaces/UserDTO";

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

  getTasks(username: string | undefined): Observable<Task[]> {
    let httpOptions = this.authService.httpOptions;
    return this.http.get<Task[]>(`${this.apiUrl}/tasks/${username}`, httpOptions)
  }

  getUsernames():Observable<UserDTO[]>{
    return this.http.get<UserDTO[]>(`${this.apiUrl}/usernames`,this.authService.httpOptions)
  }

}
