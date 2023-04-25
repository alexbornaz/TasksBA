import {Injectable} from '@angular/core';
import {AuthenticationService} from "./authentication.service";
import jwtDecode from "jwt-decode";
import {Task} from "../interfaces/Task";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiUrl: string = "http://localhost:8080/api/user"
  decodedToken ?: any;
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private authService: AuthenticationService, private http: HttpClient) {
    const token = this.authService.getToken();
    if (typeof token === "string") {
      this.decodedToken = jwtDecode(token)
    }
  }

  private addAuthorizationHeader(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.httpOptions.headers = this.httpOptions.headers.set('Authorization', `Bearer ${token}`);
    }
  }

  getUsername(): string | null {
    if (this.decodedToken) {
      return this.decodedToken.sub;
    }
    return null;
  }

  getTasks(username: string | undefined): Observable<Task[]> {
    this.addAuthorizationHeader();
    return this.http.get<Task[]>(`${this.apiUrl}/tasks/${username}`, this.httpOptions)
  }
}
