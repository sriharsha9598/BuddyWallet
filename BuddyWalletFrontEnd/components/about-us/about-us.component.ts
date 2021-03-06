import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-about-us',
  templateUrl: './about-us.component.html',
  styleUrls: ['./about-us.component.css']
})
export class AboutUsComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit() {
  }
  
  close(){
    //If user is logged in and viewing this page, router is navigated to dashboard page else router is navigated to login main page
    if(UserService.myAccountNumber==0)
      this.router.navigate(["/login"]);
    else
      this.router.navigate(["/home/profile"])
  }

}