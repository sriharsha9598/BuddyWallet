import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-report-issue',
  templateUrl: './report-issue.component.html',
  styleUrls: ['./report-issue.component.css']
})
export class ReportIssueComponent implements OnInit {

  input:boolean=true;
  success:boolean=false;
  submitted:boolean=false;
  reportForm:FormGroup
  constructor(private router:Router,private formBuilder:FormBuilder,private service:UserService,private logger:LoggingService) { }

  ngOnInit() {
    this.reportForm=this.formBuilder.group({
      name:['',Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][A-Z a-z]")],
      subject:['',Validators.required],
      message:['',Validators.required]
    })
  }

  submit(){
    this.submitted=true;
    if(this.reportForm.invalid){
      if(this.reportForm.controls.name.errors){
        document.getElementById("name").className="error";
      }
      if(this.reportForm.controls.subject.errors){
        document.getElementById("subject").className="error";
      }
      if(this.reportForm.controls.message.errors){
        document.getElementById("message").className="error";
      }
      return;
    }
    else{
      this.service.addMail(this.reportForm.value,"complaint").subscribe(data=>{
        if(data==true){
          this.input=false;
          this.success=true;
          this.logger.logStatus(this.reportForm.controls.name.value+"'s complaint has been submitted successfully")

          setTimeout(() => {
            this.close();
          }, 3000);
        }
        else{
          this.input=false;
          this.success=false;
        }
      },
      err=>{
        this.input=false;
        this.success=false;
      })
    }
  }

  tryAgain(){
    this.submitted=false;
    this.input=true;
    this.success=false;
  }

  close(){
    if(UserService.myAccountNumber==0)
      this.router.navigate(["/login"]);
    else
      this.router.navigate(["/home/profile"])
  }
}
