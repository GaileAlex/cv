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

  }

  onSubmit() {
    this.authService.login(this.form.username, this.form.password).subscribe(
      data => {
        sessionStorage.setItem('user', JSON.stringify(data.user));
        sessionStorage.setItem('accessToken', data.auth.accessToken);
        sessionStorage.setItem('roles', data.auth.refreshToken);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
        this.isLoggedIn = false;
      }
    );
    this.router.navigateByUrl('/');
  }

  reloadPage() {
    this.router.navigateByUrl('');
  //  window.location.reload();
  }
}
