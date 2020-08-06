import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    form: any = {};
    isLoggedIn = false;
    isLoginFailed = false;
    errorMessage = '';

    constructor(private router: Router, private authService: AuthService) {
    }

    ngOnInit(): void {
        window.scrollTo(0, 0);
        this.form.username = 'Test';
        this.form.password = 'testUser';
    }

    onSubmit() {
        this.authService.login(this.form.username, this.form.password).subscribe(
            data => {
                sessionStorage.setItem('user', JSON.stringify(data.user));
                sessionStorage.setItem('accessToken', data.auth.accessToken);
                sessionStorage.setItem('roles', data.auth.refreshToken);
                this.isLoginFailed = false;
                this.isLoggedIn = true;
                this.router.navigateByUrl('/blog');
            },
            err => {
                this.errorMessage = err;
                this.isLoginFailed = true;
                this.isLoggedIn = false;
            }
        );

    }

}
