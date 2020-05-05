import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { Account } from 'src/app/models/walletDB';
import { eStatement } from 'src/app/models/transactionsDB';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-admin-edit-acnt',
  templateUrl: './admin-edit-acnt.component.html',
  styleUrls: ['./admin-edit-acnt.component.css']
})
export class AdminEditAcntComponent implements OnInit {

  fromDate:Date
  toDate:Date
  editUserForm:FormGroup
  userAccount:Account
  transactions:eStatement[]
  constructor(private service:UserService, private router:Router, private formBuilder:FormBuilder,private logger:LoggingService) { }

  ngOnInit() {
    if(localStorage.userName==null){
      this.router.navigate(["/admin_login"])
    }

    this.editUserForm=this.formBuilder.group({
      accNum:['',Validators.required],
      balance:['',Validators.required],
      transCount:['',Validators.required],
      firstName: ['', [Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      middleName: ['', [ Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      lastName: ['', [Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      city: ['', [Validators.required, Validators.pattern("[A-Z][a-zA-Z]*"), Validators.minLength(3)]],
      phoneNum: ['', [Validators.required, Validators.pattern("[6-9][0-9]{9}")]],
      emailId: ['', [Validators.required, Validators.email]],
      userName: ['', [Validators.required, Validators.pattern("(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?!.*\\s).*$")]],
      password:[''],
      tranPassword:[''],
      blocked:[''],
    });
    
    this.service.eStatement().subscribe(data=>{
      this.transactions=data;
    },
    err=>{
      console.log(err.stack);
    })

    this.getAccountDetails();
  }

  getAccountDetails(){
    this.service.getAccountDetails().subscribe(data=>{
      this.userAccount=data;
      this.setValues();
    },
    err=>{
      console.log(err.stack);
    })
  }

  update(){
    this.service.updateMyAccount(this.editUserForm.value).subscribe(data=>{
      if(data){
        document.getElementById("cancelBtn").style.visibility="hidden";
        document.getElementById("saveBtn").style.visibility="hidden";
        this.logger.logStatus(this.editUserForm.controls.userName.value+"'s account updated successfully")
      }
    },err=>{
      console.log(err.stack);
    })
    
  }

  block(){
    this.userAccount.blocked=1;
    this.editUserForm.controls.blocked.setValue(1);
    this.update();
    this.logger.logStatus(this.editUserForm.controls.userName.value+"'s account is blocked!!'")
  }
   
  unBlock(){
    this.userAccount.blocked=0;
    this.editUserForm.controls.blocked.setValue(0);
    this.update();
    this.logger.logStatus(this.editUserForm.controls.userName.value+"'s account is unblocked!!'")
  }

  showButtons(){
    document.getElementById("cancelBtn").style.visibility="visible";
    document.getElementById("saveBtn").style.visibility="visible";
  }

  setValues(){
    this.editUserForm.setValue(this.userAccount);
  }

  back(){
    this.router.navigate(["/admin_home"]);
  }

  logOut(){
    localStorage.removeItem("userName");
    this.router.navigate(["/login"]);
    this.logger.logStatus("Admin logged out successfully")
  }

  cancel(){
    document.getElementById("cancelBtn").style.visibility="hidden";
    document.getElementById("saveBtn").style.visibility="hidden";
    this.setValues();
    this.logger.logStatus(this.editUserForm.controls.userName.value+"'s account updation canceled!!")
  }
}
