import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-rate-us',
  templateUrl: './rate-us.component.html',
  styleUrls: ['./rate-us.component.css']
})
export class RateUsComponent implements OnInit {

  submitted: boolean = false;
  feedbackForm: FormGroup;
  constructor(private formBuilder: FormBuilder, private router: Router, private service: UserService,private logger:LoggingService) { }
  ngOnInit() {

    if (UserService.myAccountNumber == 0) {
      this.router.navigate(["/login"]);
    }

    this.feedbackForm = this.formBuilder.group({
      subject: ['', Validators.required],
      message: ['', Validators.required],
    });
  }

  submit() {
    if (this.feedbackForm.invalid) {
      return;
    }
    else {
      this.service.addMail(this.feedbackForm.value, "feedback").subscribe(data => {
        if (data) {
          this.logger.logStatus(UserService.myAccountNumber+"'s feedback has been submitted successfully!")
          this.submitted = true;
          setTimeout(() => {
            this.close();
          }, 2000);
        }
      },
        err => {
          console.log(err.stack)
        });
    }
  }

  close() {
      this.router.navigate(["/home/profile"])
  }
}
