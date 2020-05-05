import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { __await } from 'tslib';
import { UserService } from 'src/app/services/user.service';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  leftCardSize: string = "col-lg-1 col-md-1";
  routerCardSize: string = "col-lg-10 col-md-10";
  rightCardSize: string = "col-lg-1 col-md-1";
  logoutPosition: string = "left";
  leftClosed: boolean = false;
  rightClosed: boolean = false;
  title: string;
  constructor(private router: Router, private service: UserService,private logger:LoggingService) {
  }
  ngOnInit() {
  if(UserService.myAccountNumber==0)
  {
    this.router.navigate(["/login"]);
  }

    this.title = "Hi Buddy!";
    document.getElementById("leftNavBtn").className = "disappear";
    document.getElementById("homeBtn").className = "disappear";
  }

  //To go to dashboard page
  home() {
    document.getElementById("leftNavBtn").className = "disappear";
    this.buttonDesignClear();
    document.getElementById("homeBtn").className = "disappear";
    document.getElementById("myButton1").className = "actived";
    setTimeout(() => {
      this.title = "Hi Buddy!"
      this.router.navigate(['home/profile']);
      this.openLeftNav();
    }, 500)
  }

  //To go to deposit page
  deposit() {
    document.getElementById("homeBtn").className = "appear";
    this.buttonDesignClear();
    document.getElementById("myButton2").className = "actived";
    setTimeout(() => {
      this.title = "Add Money";
      this.router.navigate(['home/deposit']);
      document.getElementById("leftNavBtn").className = "leftNavButton";
      this.closeNavs();
    }, 500)
  }

  //To go to withdraw page
  withdraw() {
    document.getElementById("homeBtn").className = "appear";
    this.buttonDesignClear();
    document.getElementById("myButton3").className = "actived";
    setTimeout(() => {
      this.title = "Withdraw Money";
      this.router.navigate(['home/withdraw']);
      document.getElementById("leftNavBtn").className = "leftNavButton";
      this.closeNavs();
    }, 500)
  }

  //To go to fund transfer page
  fundTransfer() {
    document.getElementById("homeBtn").className = "appear";
    this.buttonDesignClear();
    document.getElementById("myButton4").className = "actived";
    setTimeout(() => {
      this.title = "Fund Transfer"
      this.router.navigate(['home/fundTransfer']);
      document.getElementById("leftNavBtn").className = "leftNavButton";
      this.closeNavs();
    }, 500)
  }

  //To go to manage beneficiary page
  manageBeneficiary() {
    document.getElementById("homeBtn").className = "appear";
    this.buttonDesignClear();
    document.getElementById("myButton5").className = "actived";
    setTimeout(() => {
      this.title = "Manage Beneficiary";
      this.router.navigate(['home/manage_Beneficiary']);
      document.getElementById("leftNavBtn").className = "leftNavButton";
      this.closeNavs();
    }, 500)
  }

  //To go to eStatement page
  eStatement() {
    document.getElementById("homeBtn").className = "appear";
    this.buttonDesignClear();
    document.getElementById("myButton6").className = "actived";
    setTimeout(() => {
      this.title = "e-Statement"
      this.router.navigate(['home/eStatement']);
      document.getElementById("leftNavBtn").className = "leftNavButton";
      this.closeNavs();
    }, 500)
  }

  //To go to edit profile page
  editProfile() {
    document.getElementById("homeBtn").className = "appear";
    this.buttonDesignClear();
    document.getElementById("myButton7").className = "actived";
    setTimeout(() => {
      this.title = "Edit Profile"
      this.logoutPosition = "right";
      this.router.navigate(['home/editProfile']);
      document.getElementById("leftNavBtn").className = "leftNavButton";
      this.closeNavs();
    }, 500)
  }

  //To close menu bar and ads card
  closeNavs() {
    this.closeLeftNav();
    this.closeRightNav2();
  }

  //To close ads Card
  closeRightNav2() {
    document.getElementById("rightCard").style.width = "0";
    this.logoutPosition = "right";
    document.getElementById("hr").className = "lineExtended";

  }

  //To open ads card
  openRightNav2() {
    document.getElementById("rightCard").style.width = "100%";
    this.logoutPosition = "left";
    document.getElementById("hr").className = "line";
  }

  //To open report issue page
  openRightNav3() {
    this.router.navigate(["/report_issue"])
  }

  //To open contact us page
  openRightNav4() {
    this.router.navigate(["/contactUs"])
  }

  //To open rate us page
  openRightNav5() {
    this.router.navigate(["/rateUs"])
  }

  //To open menu bar
  openLeftNav() {
    document.getElementById("leftCard").style.width = "100%";
    document.getElementById("leftNavBtn").className = "disappear";
    document.getElementById("homeBtn").className="disappear";
  }

  //To close menu bar
  closeLeftNav() {
    document.getElementById("leftCard").style.width = "0";
    document.getElementById("leftNavBtn").className = "leftNavButton";
    document.getElementById("homeBtn").className="appear";
  }


  logout() {
    UserService.myAccountNumber = null;
    UserService.beneficiary = null;
    this.logger.logStatus("User logged out successfully")
    this.router.navigate(["/login"]);
  }

  //To make all buttons to default design from active design
  buttonDesignClear() {
    document.getElementById("myButton1").className = "designedButton btn";
    document.getElementById("myButton2").className = "designedButton btn";
    document.getElementById("myButton3").className = "designedButton btn";
    document.getElementById("myButton4").className = "designedButton btn";
    document.getElementById("myButton5").className = "designedButton btn";
    document.getElementById("myButton6").className = "designedButton btn";
    document.getElementById("myButton7").className = "designedButton btn";
  }

}