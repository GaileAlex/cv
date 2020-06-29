import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth/auth.service";

@Component({
    selector: 'registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

    constructor(private authService: AuthService) {

    }

    ngOnInit() {
        window.scrollTo(0, 0);
    }

  onSubmit() {
   /* this.authService.register(this.form).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );*/
  }
}
