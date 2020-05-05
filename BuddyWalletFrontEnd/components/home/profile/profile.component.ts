import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Account } from 'src/app/models/walletDB';
import { Router } from '@angular/router';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  
  showBalance:boolean=false;
  myAccount:Account
  loading:boolean
  constructor(private service:UserService,private router:Router,private logger:LoggingService) { }

  ngOnInit() 
  {
    this.loading=true;
    setTimeout(() => {
      this.loading=false;
    }, 2500);

    if(UserService.myAccountNumber==0){
      this.router.navigate(["/login"]);
    }


    //Getting account details to print in dashboard page
    this.service.getAccountDetails().subscribe(data=>{
      this.myAccount=data;     
      this.logger.logStatus("User is viewing dashboard page")
    },
    err=>{
      console.log(err.error.errorMessage);
    });
  }

  showMyBalance(){
    this.showBalance=true;
  }
   
  hideMyBalance(){
    this.showBalance=false;
  }
}
