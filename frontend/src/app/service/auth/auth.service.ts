import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Constants } from '../../constants/appConstants';
import { environment } from '../../../environments/environment';
import { Router } from "@angular/router";
import { map } from 'rxjs/operators';
import { User } from "../../models/user";
import { StatisticsService } from "../statistics.service";

const httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private userSubject: BehaviorSubject<User>;
    public user: Observable<User>;

    constructor(private http: HttpClient, private router: Router, private statisticsService: StatisticsService) {
        this.userSubject = new BehaviorSubject<User>(null);
        this.user = this.userSubject.asObservable();
    }

    public get userValue(): User {
        return this.userSubject.value;
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
            headers: new HttpHeaders({
                userIP: `${ sessionStorage.getItem('userIP') }`,
                userId: `${ this.statisticsService.getSessionCookie() }`

            })
        };
        return this.http.post<any>(environment.apiAuthUrl + Constants.LOGIN_URL, {
            username,
            password
        }, userIPOptions)
            .pipe(map((user) => {
                this.userSubject.next(user);

                return user;
            }));
    }

    logout() {
        sessionStorage.setItem('user', '');
        sessionStorage.setItem('accessToken', '');
        sessionStorage.setItem('roles', '');
        this.userSubject.next(null);
        this.router.navigate(['/login']);
    }

}
