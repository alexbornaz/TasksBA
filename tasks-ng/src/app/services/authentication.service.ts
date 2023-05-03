import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {RegisterReq} from "../interfaces/RegisterReq";
import {LoginReq} from "../interfaces/LoginReq";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {



  url = "http://localhost:8080/api/auth"
  private _httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient,private jwtHelper:JwtHelperService) {
  }

  login(loginReq: LoginReq): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/login`, loginReq, {observe: 'response'});
  }

  isLoggedIn(): boolean {
    console.log(!this.jwtHelper.isTokenExpired(localStorage.getItem("token")))
    return !this.jwtHelper.isTokenExpired(localStorage.getItem("token"))
  }

  logout(): void {
    localStorage.removeItem("token");
  }

  register(registerReq: RegisterReq): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/register`, registerReq, {observe: 'response'});
  }

  getToken(): string | null {
    if (this.isLoggedIn()) {
      return localStorage.getItem("token");
    }
    return null;
  }

  private addAuthorizationHeader(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this._httpOptions.headers = this._httpOptions.headers.set('Authorization', `Bearer ${token}`);
    }
  }
  get httpOptions(): { headers: HttpHeaders } {
    this.addAuthorizationHeader();
    return this._httpOptions;
  }
}
