import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Constants } from '../../constants/appConstants';
import { environment } from '../../../environments/environment';
import { UserDataService } from '../user-data.service';

const httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient, private userDataService: UserDataService) {
    }

    register(user): Observable<any> {
        return this.http.post(environment.apiAuthUrl + Constants.REGISTRATION_URL, {
            username: user.username,
            email: user.email,
            password: user.password
        }, httpOptions);
    }

    login(username, password): Observable<any> {
        const userIPOptions = {
            headers: new HttpHeaders({userIP: `${ sessionStorage.getItem('userIP') }`})
        };
        return this.http.post(environment.apiAuthUrl + Constants.LOGIN_URL, {
            username,
            password
        }, userIPOptions);
    }

    logout() {
        sessionStorage.setItem('user', '');
        sessionStorage.setItem('accessToken', '');
        sessionStorage.setItem('roles', '');
    }

    userSpy() {
        const userIPOptions = {
            headers: new HttpHeaders({
                userIP: `${ sessionStorage.getItem('userIP') }`,
                userCountry: `${ sessionStorage.getItem('userCountry') }`,
                userCity: `${ sessionStorage.getItem('userCity') }`,
                user: `${ this.userDataService.getUserName() }`
            })
        };
        return this.http.post(environment.apiAuthUrl + Constants.USER_URL, {}, userIPOptions);
    }
}
