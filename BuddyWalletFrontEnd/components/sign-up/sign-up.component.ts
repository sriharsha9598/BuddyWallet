import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { Account } from 'src/app/models/walletDB';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  oldUserName:string
  account: Account
  input: boolean = false;
  result: boolean = false;
  submitted1: boolean = false;
  submitted2: boolean = false;
  signUpForm: FormGroup;
  signUpForm2: FormGroup;
  created: boolean = true;
  statusMsg: string


  constructor(private formBuilder: FormBuilder, private router: Router, private service: UserService,private logger:LoggingService) { }

  ngOnInit() {
    this.signUpForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      middleName: ['', [ Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      lastName: ['', [Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      city: ['', [Validators.required, Validators.pattern("[A-Z][a-zA-Z]*"), Validators.minLength(3)]],
      phoneNum: ['', [Validators.required, Validators.pattern("[6-9][0-9]{9}")]],
      emailId: ['', [Validators.required, Validators.email]],

    });

    this.signUpForm2 = this.formBuilder.group({
      userName: ['', [Validators.required, Validators.pattern("(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?!.*\\s).*$")]],
      password: ['', [Validators.required, Validators.pattern("(?=^.{8,15}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$")]],
      password2: ['', Validators.required],
      tranPassword: ['', [Validators.required, Validators.pattern("(?=^.{8,15}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$")]],
      tranPassword2: ['', Validators.required]
    })
  }

  next() {
    this.submitted1 = true;
    if (this.signUpForm.invalid)
      return;
    else {
      this.input = true;
      this.result = false;
    }
  }

  back(){
    this.input=false;
    this.result=false;
  }

  signUp() {
    this.submitted2 = true;
    if (this.signUpForm2.invalid)
      return;
    else {
      if (this.signUpForm2.controls.password.value != this.signUpForm2.controls.tranPassword.value) {
        if (confirm('Are you sure creating the account?')) {
          this.service.signUp(this.signUpForm.value, this.signUpForm2.value).subscribe(data => {
            if (data == true) {
            // If response is true...Success page is shown
              this.result = true;
              this.logger.logStatus(this.signUpForm.controls.firstName.value+" account created successfully");
            }
          }, err => {
            this.oldUserName=this.signUpForm2.controls.userName.value;
            this.statusMsg = err.error.errorMessage;
            this.logger.logStatus(this.signUpForm.controls.firstName.value+" account creation failed due to "+err.error.errorMessage);
          })
        }
      }
    }
  }

  login() {
    this.router.navigate(['/login']);
  }
}
