import { Injectable } from '@angular/core';
import { User } from '../models/user';

@Injectable({
    providedIn: 'root'
})
export class UserDataService {

    constructor() {
    }

    getUserName(): string {
        const userName = sessionStorage.getItem('user') ? sessionStorage.getItem('user') : null;
        const userData = new User();
        if (userName) {
            const user: User = JSON.parse(userName);
            userData.username = user.username;
        }
        return userData.username;
    }

    getUserRole(): string {
        const userRole = sessionStorage.getItem('user') ? sessionStorage.getItem('user') : null;
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
