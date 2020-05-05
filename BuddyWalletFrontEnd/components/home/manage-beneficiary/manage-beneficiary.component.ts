import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { HomeComponent } from '../home.component';
import { Beneficiary } from 'src/app/models/beneficiaryDB';
import { error } from 'util';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-manage-beneficiary',
  templateUrl: './manage-beneficiary.component.html',
  styleUrls: ['./manage-beneficiary.component.css']
})
export class ManageBeneficiaryComponent implements OnInit {

  constructor(private service: UserService, private router: Router, private home: HomeComponent,private logger:LoggingService) { }

  statusMsg: string;
  status: string
  beneficiaries: Beneficiary[]
  ngOnInit() {
    if(UserService.myAccountNumber==0){
      this.router.navigate(["/login"]);
    }

    //To get and display all the existing beneficiaries to this account
    this.getAllBeneficiary();
    this.statusMsg = "";
  }

  getAllBeneficiary() {
    this.service.getAllBeneficiaries().subscribe(data => {
      this.beneficiaries = data;
    });
  }

  //To go to add beneficiary page
  addBeneficiary() {
    this.home.title = "Add Beneficiary";
    this.router.navigate(["/home/add_beneficiary"]);
    this.logger.logStatus("User is trying to add a new beneficiary")
  }

  //To go to edit beneficiary page
  editBeneficiary(i: number) {
    //storing the beneficiary id of selected beneficiary for further use
    UserService.beneficiary = this.beneficiaries[i];
    UserService.tempBeneficiaryIndex=i;
    this.home.title = "Edit Beneficiary";
    this.router.navigate(["/home/edit_beneficiary"]);
    this.logger.logStatus("User is trying to edit "+this.beneficiaries[i].nickName+"'s beneficiary")
  }

  //To show the status if any operation is done and automatically disappear after 10secs
  setStatusMsg(msg: string, status: string) {
    if (status == "true") {
      this.status = "success";
    }
    else if (status = "fail") {
      this.status = "danger";
    }
    this.statusMsg = msg;
    setTimeout(() => {
      this.statusMsg = "";
    }, 10000);
  }

  deleteBeneficiary(i: number) {
  //To show the status if any operation is done and automatically disappear after 10secs
    UserService.beneficiary = this.beneficiaries[i];
    UserService.tempBeneficiaryIndex=i;
    this.logger.logStatus("User is trying to delete "+this.beneficiaries[i].nickName+"'s beneficiary")

    this.service.deleteBeneficiary().subscribe(data => {
      if (data == true) {
        this.logger.logStatus(this.beneficiaries[i].nickName+"'s beneficiary is deleted")
        this.setStatusMsg(this.beneficiaries[i].nickName + " beneficiary has been deleted successfully!!", "true")
        this.getAllBeneficiary();
      }
      else {
        this.setStatusMsg(this.beneficiaries[i].nickName + " beneficiary could not be deleted at this moment!!", "false")
      }
    },
      err => {
        this.logger.logStatus(this.beneficiaries[i].nickName+"'s beneficiary deletion failed")
        this.setStatusMsg(this.beneficiaries[i].nickName + " beneficiary could not be deleted at this moment!!", "false")
      });
    setTimeout(() => {
      UserService.beneficiary = null;
    }, 10000);
  }

  //To transfer funds to a selected beneficiary
  transferNow(i: number) {
    UserService.beneficiary = this.beneficiaries[i];
    this.router.navigate(["/home/fundTransfer"])
    this.logger.logStatus("User is trying to transfer funds to "+this.beneficiaries[i].nickName+"'s beneficiary")
  }

}