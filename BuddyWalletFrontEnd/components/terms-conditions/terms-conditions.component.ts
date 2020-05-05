import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-terms-conditions',
  templateUrl: './terms-conditions.component.html',
  styleUrls: ['./terms-conditions.component.css']
})
export class TermsConditionsComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit() {
  }
  
  close(){
    if(UserService.myAccountNumber==0)
      this.router.navigate(["/login"]);
    else
      this.router.navigate(["/home/profile"])
  }

}
