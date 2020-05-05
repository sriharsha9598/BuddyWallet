import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HomeComponent } from '../home.component';
import { UserService } from 'src/app/services/user.service';
import { Beneficiary } from 'src/app/models/beneficiaryDB';
import { FormGroup, FormBuilder, Validators, PatternValidator } from '@angular/forms';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-fund-transfer',
  templateUrl: './fund-transfer.component.html',
  styleUrls: ['./fund-transfer.component.css']
})
export class FundTransferComponent implements OnInit {
  message: string;
  beneficiaries: Beneficiary[] = null;
  wrong: boolean;
  loading: boolean;
  submitted: boolean;
  fundTransferForm: FormGroup;
  result: boolean;
  input: boolean;
  selectedBeneficiaryIndex: number;
  statusMsg: string;
  errorCount: number = 3;
  constructor(private formBuilder: FormBuilder, private router: Router, private service: UserService, private homeObj: HomeComponent,private logger:LoggingService) { }
  ngOnInit() {
    if(UserService.myAccountNumber==0){
      this.router.navigate(["/login"]);
    }
    
    this.fundTransferForm = this.formBuilder.group({
      beneficiaryIndex: ['', Validators.required],
      amount: ['',Validators.min(1)],
      message: ['']
    });

    this.result = false;
    this.input = false;
    this.submitted = false;

    this.service.getAllBeneficiaries().subscribe(data => {
      this.beneficiaries = data;
      if (UserService.beneficiary) {
        this.selectedBeneficiaryIndex = UserService.beneficiary.id;
      }
    });
  }

  submit() {
    this.submitted = true;
    if (this.fundTransferForm.invalid) {
      return;
    }
    else {
      this.input = true;
      this.result = false;
    }

  }

  fundTransfer(tranPassword: string) {
    //if transaction password is not null
    if (tranPassword) {
      document.getElementById("verifyButton").style.display = "none";
      this.loading = true;
      //if message is null, setting value as "NA" so that later in backend it will be considered as no message
      if (!this.fundTransferForm.controls.message.value) {
        this.fundTransferForm.controls.message.setValue("NA")
      }
      this.service.fundTransfer(tranPassword, this.fundTransferForm.controls.beneficiaryIndex.value, this.fundTransferForm.controls.amount.value, this.fundTransferForm.controls.message.value).subscribe(data => {
        if (data) {
          //receiving response and displaying
          this.statusMsg = data;
          UserService.beneficiary=null;
          this.result = true;
          this.input = true;
          this.logger.logStatus("Fund Transfer is successful")
        }
      },
        err => {
          //if incorrect transaction password, giving the user 3chances to enter a correct transaction password
          if (err.error.errorMessage == "Incorrect Transaction Password!!") {
            if (this.errorCount > 1) {
              this.logger.logStatus("User entered an incorrect transaction password")
              this.wrong = false;
              document.getElementById("tranPasswordInputBox").style.border = "none";
              document.getElementById("tranPasswordInputBox").style.boxShadow = "0 4px 8px 0 rgba(0, 0, 0, 0.2)";
              this.statusMsg = "";
              this.errorCount--;
              setTimeout(() => {
                this.loading = false;
                this.wrong = true;
                document.getElementById("verifyButton").style.display = "inline";
                document.getElementById("tranPasswordInputBox").style.border = "1px solid red";
                document.getElementById("tranPasswordInputBox").style.boxShadow = "0px 0px 6px 0px  red";
                this.statusMsg = "Password Invalid!! Attempts left : 0" + this.errorCount;
              }, 2500);
            }
            //if user enters wrong transaction password after 3chances also, transaction will be failed
            else {
              this.statusMsg = err.error.errorMessage;
              UserService.beneficiary=null;
              this.result = true;
              this.input = false;
              this.logger.logStatus("Fund Transfer failed due to to incorrect transaction password")
            }
          }
          //if any error other than incorrect transaction password
          else {
            UserService.beneficiary=null;
            this.statusMsg = err.error.errorMessage;
            this.result = true;
            this.input = false;
            this.logger.logStatus("Fund Transfer failed")
          }
        });
    }
    //if transaction password is null
    else {
      this.wrong = false;
      document.getElementById("tranPasswordInputBox").style.border = "none";
      document.getElementById("tranPasswordInputBox").style.boxShadow = "0 4px 8px 0 rgba(0, 0, 0, 0.2)";
      this.statusMsg = "";
      setTimeout(() => {
        this.wrong = true;
        document.getElementById("tranPasswordInputBox").style.border = "1px solid red";
      }, 200);
    }
  }

  //for another transfer option
  transferMore(){
    this.input=false;
    this.result=false;
    this.submitted=false;
    this.loading=false;
    this.statusMsg="";
    this.fundTransferForm.controls.beneficiaryIndex.setValue(null);
    this.fundTransferForm.controls.amount.setValue(null);
    this.fundTransferForm.controls.message.setValue(null);
    this.selectedBeneficiaryIndex=null;
    this.logger.logStatus("User is trying to do another fund transfer")
  }

  home() {
    UserService.beneficiary = null;
    this.homeObj.home();
  }

  addNewBeneficiary() {
    this.router.navigate(["/home/add_beneficiary"])
    this.logger.logStatus("User is trying to add a new beneficiary")

  }
}
