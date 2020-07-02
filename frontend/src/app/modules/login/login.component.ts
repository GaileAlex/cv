import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];


  constructor(private router: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    window.scrollTo(0, 0);
    /*if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }*/
  }

  onSubmit() {
    this.authService.login(this.form.username, this.form.password).subscribe(
      data => {
       /* this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);*/

        this.isLoginFailed = false;
        this.isLoggedIn = true;
       /* this.roles = this.tokenStorage.getUser().roles;*/
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage() {
    this.router.navigateByUrl('');
  //  window.location.reload();
  }
}
