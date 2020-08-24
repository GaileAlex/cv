import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user';

@Injectable({
    providedIn: 'root'
})
export class UserDataService {

    constructor(private http: HttpClient) {
        this.getUserIP();
    }

    getUserIP() {
        this.http.get('https://ipapi.co/json/').subscribe((res: any) => {
            sessionStorage.setItem('userIP', JSON.stringify(res.ip));
            sessionStorage.setItem('userCountry', JSON.stringify(res.country_name));
            sessionStorage.setItem('userCity', JSON.stringify(res.city));
        });
    }

    getUserName(): string {
        const userName = sessionStorage.getItem('user') ? sessionStorage.getItem('user')  : null;
        const userData = new User();
        if (userName) {
            const user: User = JSON.parse(userName);
            userData.username = user.username;
        }
        return userData.username;
    }

    getUserRole(): string {
        const userRole = sessionStorage.getItem('user') ? sessionStorage.getItem('user')  : null;
        const userData = new User();
        if (userRole) {
            const user: User = JSON.parse(userRole);
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
}
