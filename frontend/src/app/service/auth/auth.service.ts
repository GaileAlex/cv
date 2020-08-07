import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Constants } from '../../constants/appConstants';
import { User } from '../../models/user';
import { environment } from '../../../environments/environment';

const httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    ipAddress: string;

    constructor(private http: HttpClient) {
        this.getUserIP();
    }

    register(user): Observable<any> {
        return this.http.post(environment.apiAuthUrl + Constants.REGISTRATION_URL, {
            username: user.username,
            email: user.email,
            password: user.password
        }, httpOptions);
    }

    login(username, password): Observable<any> {
        const httpOptions = {
            headers: new HttpHeaders({'userIP': `${ this.ipAddress }`})
        };
        return this.http.post(environment.apiAuthUrl + Constants.LOGIN_URL, {
            username,
            password
        }, httpOptions);
    }

    logout() {
        sessionStorage.setItem('user', '');
        sessionStorage.setItem('accessToken', '');
        sessionStorage.setItem('roles', '');
    }

    getUserName(): string {
        const userString = sessionStorage.getItem('user');
        const userData = new User();
        if (userString) {
            const user: User = JSON.parse(userString);
            userData.username = user.username;
        }
        return userData.username;
    }

    getUserRole(): string {
        const userString = sessionStorage.getItem('user');
        const userData = new User();
        if (userString) {
            const user: User = JSON.parse(userString);
            userData.role = user.role;
        }
        return userData.role;
    }

    isAuthenticated(): boolean {
        return !!sessionStorage.getItem('accessToken') && !!sessionStorage.getItem('user');
    }

    isTokenPresent(): boolean {
        return !!sessionStorage.getItem('accessToken');
    }

    getUserIP() {
        return this.http.get('https://jsonip.com/').subscribe((res: any) => {
            this.ipAddress = res.ip;
        });
    }
}
