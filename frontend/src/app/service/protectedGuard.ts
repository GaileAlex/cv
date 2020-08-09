import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth/auth.service';
import { UserDataService } from './user-data.service';

@Injectable({
    providedIn: 'root'
})
export class ProtectedGuardService implements CanActivate {

    constructor(public auth: AuthService, public router: Router, private userDataService: UserDataService) {
    }

    canActivate(): boolean {
        if (!this.userDataService.isAuthenticated()) {
            this.router.navigate(['/login']);
            return false;
        }
        return true;
    }
}
