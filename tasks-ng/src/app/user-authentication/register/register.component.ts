import {Component} from '@angular/core';
import {AuthenticationService} from "../../authentication.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RegisterReq} from "../../interfaces/RegisterReq";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  serverMessage: String = "";
  showMessage: boolean = false;

  constructor(private authService: AuthenticationService, private router: Router,) {

  }

  registerForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(3)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)])
  });

  onSubmit() {
    if (this.registerForm.valid) {
      const registerReq: RegisterReq = {
        username: this.registerForm.value.username ?? "",
        email: this.registerForm.value.email ?? "",
        password: this.registerForm.value.password ?? ""
      };
      this.authService.register(registerReq).subscribe(
        response => {
          const token: string = response.headers.get("Authorization") as string;
          if (!token) {
            this.serverMessage = response.body.message;
            this.showMessage = true;
          } else {
            localStorage.setItem("token", token);
            this.router.navigate(['']);
          }
        }
      )
    }
  }
}
