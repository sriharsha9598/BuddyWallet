import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { HomeComponent } from '../home.component';
import { ManageBeneficiaryComponent } from '../manage-beneficiary/manage-beneficiary.component';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-add-beneficiary',
  templateUrl: './add-beneficiary.component.html',
  styleUrls: ['./add-beneficiary.component.css']
})
export class AddBeneficiaryComponent implements OnInit {
  submitted: boolean;
  beneficiaryForm: FormGroup;
  success:boolean;
  input:boolean;
  constructor(private formBuilder: FormBuilder, private router: Router, private service: UserService, private homeObj:HomeComponent,private logger:LoggingService) { }
  ngOnInit() {
    this.beneficiaryForm = this.formBuilder.group({
      name: ['', Validators.required],
      bAccNum: ['', Validators.required],
      nickName: ['', Validators.required],
      category: ['']
    });

    this.input=true;
    this.submitted=false;
  }

  addBeneficiary(){
    this.submitted=true;
    if(this.beneficiaryForm.invalid){
      if(this.beneficiaryForm.controls.name.errors){
        document.getElementById("name").className="error";
      }
      if(this.beneficiaryForm.controls.bAccNum.errors){
        document.getElementById("bAccNum").className="error";
      }
      if(this.beneficiaryForm.controls.nickName.errors){
        document.getElementById("nickName").className="error";
      }
      return;
    }
    else{
      this.service.addBeneficiary(this.beneficiaryForm.value).subscribe(data=>{
        if(data==true){
          this.input=false;
          this.success=true;
          this.logger.logStatus(this.beneficiaryForm.controls.nickName.value+"'s beneficiary has been added succesfully!!")
        }
        else{
          this.input=false;
          this.success=false;
        }
      },
      err=>{
        this.input=false;
        this.success=false;
        this.logger.logStatus(this.beneficiaryForm.controls.nickName.value+"'s beneficiary failed to get added")
      })
    }
  }

  anotherBeneficiary(){
    this.submitted=false;
    this.success=false;
    this.input=true;
    this.logger.logStatus("User trying to add another beneficiary")

  }
  
  home(){
    this.homeObj.manageBeneficiary();
  }
  
}
