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
        this.http.get('https://jsonip.com/').subscribe((res: any) => {
            sessionStorage.setItem('userIP', JSON.stringify(res.ip));
        });
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
}
