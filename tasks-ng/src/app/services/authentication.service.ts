import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {RegisterReq} from "../interfaces/RegisterReq";
import {LoginReq} from "../interfaces/LoginReq";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  url = "http://localhost:8080/api/auth"
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  login(loginReq: LoginReq): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/login`, loginReq, {observe: 'response'});
  }

  isLoggedIn(): boolean {
    return localStorage.getItem("token") != null;
  }

  logout(): void {
    localStorage.removeItem("token");
  }

  register(registerReq: RegisterReq): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/register`, registerReq, {observe: 'response'});
  }

  getToken(): string | null {
    if (this.isLoggedIn()){
      return localStorage.getItem("token");
    }
    return null;
  }
}
