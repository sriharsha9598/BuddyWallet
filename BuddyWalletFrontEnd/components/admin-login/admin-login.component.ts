import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-admin-login',
  templateUrl: './admin-login.component.html',
  styleUrls: ['./admin-login.component.css']
})
export class AdminLoginComponent implements OnInit {

  statusMsg: string;
  submitted:boolean=false;
  adminLoginForm: FormGroup;
  constructor(private router: Router, private formBuilder: FormBuilder,private logger:LoggingService) { }

  ngOnInit() {
    this.adminLoginForm = this.formBuilder.group({
      adminName: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  login() {
    this.submitted=true;
    if (this.adminLoginForm.invalid){
      return;
    }
    else {
      this.statusMsg = "";
      if (this.adminLoginForm.controls.adminName.value == "admin" && this.adminLoginForm.controls.password.value == "admin123") {
        localStorage.userName="admin";
        this.logger.logStatus("Admin logged in successfully")
        this.router.navigate(["/admin_home"]);
      }
      else {
        setTimeout(() => {
          this.statusMsg = "Invalid Credentials!!"
          this.logger.logStatus("Admin login failed due to Invalid Credentials")
        }, 1000);
      }
    }
  }

  signUp() {
    this.router.navigate(['/signUp']);
  }

  userLogin() {
    this.router.navigate(['/login']);
  }
}
