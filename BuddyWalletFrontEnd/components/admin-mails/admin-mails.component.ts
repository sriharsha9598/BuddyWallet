import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { Mails } from 'src/app/models/MailDB';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-admin-mails',
  templateUrl: './admin-mails.component.html',
  styleUrls: ['./admin-mails.component.css']
})

export class AdminMailsComponent implements OnInit {

  allMails:Mails[]
  activeBtn:string[]=["","outline-","outline-"];
  category:string="complaint";
  constructor(private service:UserService,private router:Router,private logger:LoggingService) { }

  ngOnInit() {
    if(localStorage.userName==null){
      this.router.navigate(["/admin_login"])
    }

    this.service.getAllMails().subscribe(data=>{
      this.allMails=data
    },
    err=>{
      console.log(err.stack);
    })
  }

  changeCategory(category:string){
    this.category=category;
    if(category=="complaint"){
      this.activeBtn=["","outline-","outline-"]
      this.logger.logStatus("Admin is viewing Complaints")
    }
    else if(category=="helpMail"){
      this.activeBtn=["outline-","","outline-"]
      this.logger.logStatus("Admin is viewing Mails")
    }
    else if(category=="feedback"){
      this.activeBtn=["outline-","outline-",""]
      this.logger.logStatus("Admin is viewing feedbacks")
    }    
  }

  goToUsersPage(){
    this.router.navigate(["/admin_home"])
    this.logger.logStatus("Admin is viewing Users page")
  }

  logOut()
  {
    localStorage.removeItem("userName");
    this.router.navigate(['/login'])
    this.logger.logStatus("Admin logged out successfully")
  }

}
