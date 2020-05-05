import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { HomeComponent } from '../home.component';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-withdraw',
  templateUrl: './withdraw.component.html',
  styleUrls: ['./withdraw.component.css']
})
export class WithdrawComponent implements OnInit {
  errorCount: number = 3;
  input: boolean;
  result: boolean;
  amount: number;
  selected: boolean;
  wrong: boolean = false;
  loading: boolean = false;
  statusMsg: string;
  constructor(private router: Router, private service: UserService, private profile: HomeComponent,private logger:LoggingService) { }

  ngOnInit() {

    if(UserService.myAccountNumber==0){
      this.router.navigate(["/login"]);
    }

    this.selected = false;
    this.input = false;
    this.result = false;
  }

  withdraw100() {
    this.selected = true;
    this.amount = 100;
  }
  withdraw250() {
    this.selected = true;
    this.amount = 250;
  }
  withdraw500() {
    this.selected = true;
    this.amount = 500;
  }
  withdraw1000() {
    this.selected = true;
    this.amount = 1000;
  }
  withdraw2000() {
    this.selected = true;
    this.amount = 2000;
  }

  submit() {
    this.input = true;
    this.result = false;
  }

  withdraw(tranPassword: string) {
    //if transaction password is not null 
    if (tranPassword) {
      document.getElementById("verifyButton").style.display = "none";
      this.loading = true;
      this.service.withdraw(tranPassword, this.amount).subscribe(data => {
        if (data) {
          //receiving response and displaying
          this.statusMsg = data;
          this.result = true;
          this.input = true;
          this.logger.logStatus("Withdraw Operation is successful")
        }
      },
        err => {
          //if incorrect trarnsaction password, giving the user 3chances to enter a correct transaction password
          if (err.error.errorMessage == "Incorrect Transaction Password!!") {
            if (this.errorCount > 1) {
              this.logger.logStatus("User has entered an incorrect transaction password")
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
              this.result = true;
              this.input = false;
              this.logger.logStatus("Withdraw transaction failed due to incorrect transaction password")
            }
          }
          //if any error other than incorrect transaction password
          else {
            this.statusMsg = err.error.errorMessage;
            this.result = true;
            this.input = false;
            this.logger.logStatus("Withdraw operation failed")
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

  home() {
    this.profile.home();
  }

  //To try withdraw operation again when transaction is failed
  tryAgain() {
    this.wrong = false;
    this.input = false;
    this.result = false;
    this.logger.logStatus("User is trying to withdraw again")
  }
} 
