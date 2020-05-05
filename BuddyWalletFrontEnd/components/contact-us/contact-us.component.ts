import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.css']
})
export class ContactUsComponent implements OnInit {
  input:boolean=true;
  mail:boolean=false;
  submitted:boolean=false;
  success:boolean=null;
  helpMailForm:FormGroup
  constructor(private router: Router,private formBuilder:FormBuilder,private service:UserService) { }

  ngOnInit() {
    this.helpMailForm=this.formBuilder.group({
      name:['',Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][A-Z a-z]")],
      subject:['',Validators.required],
      message:['',Validators.required]
    })
  }

  submit(){
    this.submitted=true;
    if(this.helpMailForm.invalid){
      if(this.helpMailForm.controls.name.errors){
        document.getElementById("name").className="error";
      }
      if(this.helpMailForm.controls.subject.errors){
        document.getElementById("subject").className="error";
      }
      if(this.helpMailForm.controls.message.errors){
        document.getElementById("message").className="error";
      }
      return;
    }
    else{
      this.service.addMail(this.helpMailForm.value,"helpMail").subscribe(data=>{
        if(data==true){
          this.success=true;
          this.input=false;
          setTimeout(() => {
            this.helpMailForm.controls.name.setValue("");
            this.helpMailForm.controls.subject.setValue("");
            this.helpMailForm.controls.message.setValue("");
            this.mail=false;
            this.submitted=false;
            this.success=null;
            this.input=true;
          }, 3000);
        }
        else{
          this.success=false;
        }
      },
      err=>{
        this.success=false;
      })
    }
  }

  tryAgain(){
    this.submitted=false;
    this.mail=true;
    this.success=null;
    this.input=true;
  }

  back(){
    this.mail=false;
    this.submitted=false;
    this.success=null;
    this.input=true;
  }

 close(){
   //If user is logged in, router will navigate to dashboard page else router is navigated to login main page
    if(UserService.myAccountNumber==0)
      this.router.navigate(["/login"]);
    else
      this.router.navigate(["/home/profile"])
  }

  mailNow(){
    this.mail=true;
    this.input=true;
  }
}
