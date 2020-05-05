import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserService } from 'src/app/services/user.service';
import { Account } from 'src/app/models/walletDB';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoggingService } from 'src/app/models/loggingService';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  // selectedFile: File;
  // retrievedImage: any;
  // base64Data: any;
  // retrieveResonse: any;
  // message: string;
  // imageName: any;
  // image:any

  // passwordType:string="password"
  // tranPassType:string="password"
  name: string;
  transCount: number;
  balance: number;
  accNum: number;
  userName: string;
  myAccount: Account
  profileForm: FormGroup
  constructor(private http: HttpClient, private service: UserService, private formbuilder: FormBuilder,private logger:LoggingService) { }

  ngOnInit() {
    this.profileForm = this.formbuilder.group({
      accNum: ['', Validators.required],
      balance: ['', Validators.required],
      firstName: ['', [Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      middleName: ['', [Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      lastName: ['', [Validators.required, Validators.minLength(3), Validators.pattern("[A-Z][a-z A-Z]*")]],
      city: ['', [Validators.required, Validators.pattern("[A-Z][a-zA-Z]*"), Validators.minLength(3)]],
      phoneNum: ['', [Validators.required, Validators.pattern("[6-9][0-9]{9}")]],
      emailId: ['', [Validators.required, Validators.email]],
      userName: ['', [Validators.required, Validators.pattern("(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?!.*\\s).*$")]],
      password: ['', [Validators.required, Validators.pattern("(?=^.{8,15}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$")]],
      tranPassword: ['', [Validators.required, Validators.pattern("(?=^.{8,15}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$")]],
      transCount: ['', Validators.required]
    });
    this.getMyAccountDetails();

  }

  getMyAccountDetails() {
    this.service.getAccountDetails().subscribe(data => {
      this.myAccount = data;
      // this.profileForm.setValue(this.myAccount)
      this.setValues();
    },
      err => {
        console.log(err.stack)
      });
  }

  activateButtons() {
    document.getElementById("button1").style.visibility = "visible";
    document.getElementById("button2").style.visibility = "visible";
  }

  cancel() {
    this.setValues();
    this.logger.logStatus(this.profileForm.controls.firstName.value+"'s account update canceled")
  }

  save() {
    // if(this.myAccount.password==this.profileForm.controls.password.value || this.myAccount.tranPassword==this.profileForm.controls.tranPassword.value){
    this.service.updateMyAccount(this.profileForm.value).subscribe(data => {
      if (data) {
        this.getMyAccountDetails();
        document.getElementById("button1").style.visibility = "hidden";
        document.getElementById("button2").style.visibility = "hidden";
        this.logger.logStatus(this.profileForm.controls.firstName.value+"'s account has been updated succesfully!!")
      }
    })
  }
  // else{
  //   if(this.myAccount.password!=this.profileForm.controls.password.value){
  //     this.passwordChange=true;
  //   }
  //   if(this.myAccount.tranPassword!=this.profileForm.controls.tranPassword.value){
  //     this.tranPasswordChange=true;
  //   }
  // }
  // }
  setValues() {
    this.profileForm.controls.accNum.setValue(this.myAccount.accNum)
    this.profileForm.controls.balance.setValue(this.myAccount.balance)
    this.profileForm.controls.transCount.setValue(this.myAccount.transCount)
    this.profileForm.controls.firstName.setValue(this.myAccount.firstName)
    this.profileForm.controls.middleName.setValue(this.myAccount.middleName)
    this.profileForm.controls.lastName.setValue(this.myAccount.lastName)
    this.profileForm.controls.userName.setValue(this.myAccount.userName)
    this.profileForm.controls.transCount.setValue(this.myAccount.transCount)
    this.profileForm.controls.city.setValue(this.myAccount.city)
    this.profileForm.controls.emailId.setValue(this.myAccount.emailId)
    this.profileForm.controls.phoneNum.setValue(this.myAccount.phoneNum)
    this.profileForm.controls.password.setValue(this.myAccount.password)
    this.profileForm.controls.tranPassword.setValue(this.myAccount.tranPassword)
  }



  //Gets called when the user selects an image
  // public onFileChanged(event) {
  //   //Select File
  //   this.selectedFile = event.target.files[0];
  // }


  //Gets called when the user clicks on submit to upload the image
  // onUpload() {
  //   console.log(this.selectedFile);

  //   //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
  //   const uploadImageData = new FormData();
  //   uploadImageData.append('imageFile', this.selectedFile);

  //   //Make a call to the Spring Boot Application to save the image
  //   this.http.post('http://localhost:8094/api/uploadDp/' + UserService.myAccountNumber, uploadImageData).subscribe(data=>{
  //     this.image=data;
  //   },
  //   err=>
  //   {
  //     console.log(err.error)
  //   });


  // }

}
