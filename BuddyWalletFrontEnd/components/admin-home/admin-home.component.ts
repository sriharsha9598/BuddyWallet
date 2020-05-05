import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { Account } from 'src/app/models/walletDB';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {

  users: Account[];
  searchText = ["", "", "", "", ""];
  constructor(public service: UserService, private router: Router,private logger:LoggingService) { }

  ngOnInit() {
    if (localStorage.userName == null)
      this.router.navigate(["/admin_login"])
    else {
      this.service.getAllUsers().subscribe(data => {
        this.users = data
      });
    }
  }

  editUser(accountNumber: number) {
    UserService.myAccountNumber = accountNumber;
    this.logger.logStatus(UserService.myAccountNumber+"'s account is viewed!!'")
    this.router.navigate(["/admin_editAcnt"])
  }

  goToMailsPage() {
    this.logger.logStatus("Admin is viewing the Mails!!")
    this.router.navigate(["/admin_mails"])
  }

  logOut() {
    this.logger.logStatus("Admin logged out successfully")
    localStorage.removeItem("userName");
    this.router.navigate(['/login'])
  }
}
