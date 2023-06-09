import {Component} from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginReq} from "../../interfaces/LoginReq";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  serverMessage: string;
  showServerMessage: boolean

  constructor(private authService: AuthenticationService, private router: Router) {
    this.serverMessage = "";
    this.showServerMessage = false;
  }

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(3)]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)])
  });

  onSubmit() {
    if (this.loginForm.valid) {
      const loginReq: LoginReq = {
        username: this.loginForm.value.username ?? "",
        password: this.loginForm.value.password ?? ""
      };
      this.authService.login(loginReq)
        .subscribe(
          {
            next: () => {
              this.router.navigate(['']);
            },
            error: (error) => {
              this.serverMessage = error;
              this.showServerMessage = true;
              this.loginForm.reset();
            }
          }
        );
    }
  }
}
