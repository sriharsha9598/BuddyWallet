import { Component, OnInit } from '@angular/core';
import { eStatement } from 'src/app/models/transactionsDB';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { HomeComponent } from '../home.component';

@Component({
  selector: 'app-e-statement',
  templateUrl: './e-statement.component.html',
  styleUrls: ['./e-statement.component.css']
})
export class EStatementComponent implements OnInit {
  fromDate:Date;
  toDate:Date;
  trans:eStatement[]
  constructor(private service:UserService,private router:Router,private profile:HomeComponent) { }

  ngOnInit() {
    this.service.eStatement().subscribe(data => {
    this.trans=data;
    },
      err => {
        // on reject or on error
        console.log(err.stack);
      });
  }
  
  home()
  {
    this.profile.home();    
  }

}
