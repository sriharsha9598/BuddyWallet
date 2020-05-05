import { Component, OnInit } from '@angular/core';
import { Router, UrlSerializer } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { HomeComponent } from '../home.component';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent implements OnInit {
  input: boolean;
  result: boolean;
  amount: number;
  add: boolean;
  loading: boolean = false;
  wrong: boolean = false;
  errorCount: number;
  statusMsg: string;
  constructor(private router: Router, private service: UserService, private profile: HomeComponent,private logger:LoggingService) { }

  ngOnInit() {
    if(UserService.myAccountNumber==0){
      this.router.navigate(["/login"]);
    }

    this.add = false;
    this.input = false;
    this.result = false;
    this.errorCount = 3;
    this.statusMsg = "";
  }

  add100() {
    this.add = true;
    this.amount = 100;
  }
  add250() {
    this.add = true;
    this.amount = 250;
  }
  add500() {
    this.add = true;
    this.amount = 500;
  }
  add1000() {
    this.add = true;
    this.amount = 1000;
  }
  add2000() {
    this.add = true;
    this.amount = 2000;
  }

  submit() {
    this.input = true;
    this.result = false;
  }

  deposit(tranPassword: string) {
    if (tranPassword) {
      document.getElementById("verifyButton").style.display = "none";
      this.loading = true;
      this.service.deposit(tranPassword, this.amount).subscribe(data => {
        if(data){
          //receiving response and displaying
          this.statusMsg = data;
          this.result = true;
          this.input = true;
          this.logger.logStatus("Deposit operation is successful")
        }
        },
        err => {
          //if incorrect trarnsaction password, giving the user 3chances to enter a correct transaction password
          if (err.error.errorMessage == "Incorrect Transaction Password!!") {
            if (this.errorCount > 1) {
              this.logger.logStatus("User entered an incorrect Transaction Password")
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
              this.logger.logStatus("Deposit Transaction is failed due to wrong transaction password")
            }
          }
          //if any error other than incorrect transaction password
          else {
            this.statusMsg = err.error.errorMessage;
            this.result = true;
            this.input = false;
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

  //if another deposit operation 
  addMore() {
    this.amount = null;
    this.add = false;
    this.result = false;
    this.input = false;
    this.statusMsg="";
    this.loading=false;
    this.logger.logStatus("User trying to deposit more money")

  }

  home() {
    this.profile.home();
  }
}
