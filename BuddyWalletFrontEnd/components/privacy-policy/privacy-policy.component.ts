import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-privacy-policy',
  templateUrl: './privacy-policy.component.html',
  styleUrls: ['./privacy-policy.component.css']
})
export class PrivacyPolicyComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit() {
  }

  close(){
    if(UserService.myAccountNumber==0)
    {
      this.router.navigate(["/login"])
    }
    else{
      this.router.navigate(["/home/profile"])
    }
  }

}
