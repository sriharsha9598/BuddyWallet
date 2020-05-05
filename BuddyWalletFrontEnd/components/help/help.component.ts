import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-help',
  templateUrl: './help.component.html',
  styleUrls: ['./help.component.css']
})
export class HelpComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit() {
  }

  close(){
    // If user is logged in, router will navigate to dashboard page else router is navigated to login main page
    if(UserService.myAccountNumber==0)
      this.router.navigate(["/login"]);
    else
      this.router.navigate(["/home/profile"])
  }
}
