import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { HomeComponent } from '../home.component';
import { ManageBeneficiaryComponent } from '../manage-beneficiary/manage-beneficiary.component';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-edit-beneficiary',
  templateUrl: './edit-beneficiary.component.html',
  styleUrls: ['./edit-beneficiary.component.css']
})
export class EditBeneficiaryComponent implements OnInit {
  submitted: boolean;
  beneficiaryForm: FormGroup;
  success: boolean;
  input: boolean;
  constructor(private formBuilder: FormBuilder, private router: Router, private service: UserService, private home: HomeComponent,private logger:LoggingService) { }
  ngOnInit() {
    this.beneficiaryForm = this.formBuilder.group({
      name: ['', Validators.required],
      bAccNum: ['', Validators.required],
      nickName: ['', Validators.required],
      category: ['']
    });
    this.beneficiaryForm.controls.name.setValue(UserService.beneficiary.name);
    this.beneficiaryForm.controls.bAccNum.setValue(UserService.beneficiary.bAccNum);
    this.beneficiaryForm.controls.nickName.setValue(UserService.beneficiary.nickName);
    this.beneficiaryForm.controls.category.setValue(UserService.beneficiary.category);
    this.input = true;
    this.submitted = false;
  }

  update() {
    this.submitted = true;
    if (this.beneficiaryForm.invalid) {
      if (this.beneficiaryForm.controls.name.errors) {
        document.getElementById("name").className = "error";
      }
      if (this.beneficiaryForm.controls.name.errors) {
        document.getElementById("bAccNum").className = "error";
      }
      if (this.beneficiaryForm.controls.name.errors) {
        document.getElementById("nickName").className = "error";
      }
      return;
    }
    else {
      this.service.editBeneficiary(this.beneficiaryForm.value).subscribe(data => {
        if (data == true) {
          this.input = false;
          this.success = true;
          this.logger.logStatus(this.beneficiaryForm.controls.nickName.value+"'s beneficiary has been updated succesfully!!")
          setTimeout(() => {
            this.goBack();
          }, 2000);
        }
        else {
          this.input = false;
          this.success = false;
          this.logger.logStatus(this.beneficiaryForm.controls.nickName.value+"'s beneficiary update failed")
          setTimeout(() => {
            this.goBack();
          }, 2000);
        }
      },
        err => {
          this.input = false;
          this.success = false;
          this.logger.logStatus(this.beneficiaryForm.controls.nickName.value+"'s beneficiary update failed")
          setTimeout(() => {
            this.goBack();
          }, 2000);
        });
    }
  }

  delete() {
    this.service.deleteBeneficiary().subscribe(data=>{
      if(data){
        this.logger.logStatus(this.beneficiaryForm.controls.nickName.value+"'s beneficiary has been deleted succesfully!!")
      }
    });

    this.goBack();
  }

  goBack() {
    this.home.manageBeneficiary();
  }


}
