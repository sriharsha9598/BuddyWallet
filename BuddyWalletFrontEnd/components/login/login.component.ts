import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { LoggingService } from 'src/app/models/loggingService';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers:[LoggingService]
})
export class LoginComponent implements OnInit {
  msg: string
  submitted: boolean = false;
  loginForm: FormGroup;
  loading: boolean;
  oldUserName:string;
  oldPassword:string;
  constructor(private formBuilder: FormBuilder, private router: Router, private service: UserService, private logger:LoggingService) { }
  ngOnInit() {

    this.loginForm = this.formBuilder.group({
      userName: ['', Validators.required],
      password: ['', Validators.required],
    });
  }
  signUp() {
    this.router.navigate(['/signUp']);
  }

  adminLogin() {
    this.router.navigate(['/admin_login']);
  }

  login() {
    this.submitted = true;
    if (this.loginForm.invalid)
      return;
    else {
      document.getElementById("loginButton").style.display="none";
      this.loading=true;
      setTimeout(() => {
        this.service.login(this.loginForm.controls.userName.value, this.loginForm.controls.password.value).subscribe(data => {
          if (data != null) {
            UserService.myAccountNumber = data;
            this.router.navigate(['/home/profile']);

          }
          this.logger.logStatus(this.loginForm.controls.userName.value+" Login successful")
        },
          err => {
            this.loading=false
            document.getElementById("loginButton").style.display="inline";
            this.logger.logStatus(this.loginForm.controls.userName.value+" Login is unsuccessful!!")
            this.msg = err.error.errorMessage;
            this.oldUserName=this.loginForm.controls.userName.value
            this.oldPassword=this.loginForm.controls.password.value
            this.submitted = false;
          });
      }, 2000);
    }
  }
}
