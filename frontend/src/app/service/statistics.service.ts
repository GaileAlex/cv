import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { UserDataService } from './user-data.service';
import { Observable } from 'rxjs';
import { VisitStatisticGraph } from '../models/visitStatisticGraph';
import { formatDate } from "@angular/common";
import Cookies from "universal-cookie";
import * as moment from 'moment';

@Injectable({
    providedIn: 'root'
})
export class StatisticsService {
    private readonly USER_STATISTICS_URL: string;
    private readonly GRAPH_STATISTICS_URL: string;
    private readonly USER_OUT_STATISTICS_URL: string;
    private readonly SESSION_USER_ID_COOKIE = 'session_user-id_cookie';

    cookies = new Cookies();

    constructor(private http: HttpClient, private userDataService: UserDataService) {
        this.USER_STATISTICS_URL = environment.apiUrl + '/statistic/user';
        this.USER_OUT_STATISTICS_URL = environment.apiUrl + '/statistic/user-out';
        this.GRAPH_STATISTICS_URL = environment.apiUrl + '/statistic/graph';
    }

    userSpy() {
        this.http.get('https://ipapi.co/json/').subscribe((res: any) => {
            sessionStorage.setItem('userIP', JSON.stringify(res.ip));
            sessionStorage.setItem('userCountry', JSON.stringify(res.country_name));
            sessionStorage.setItem('userCity', JSON.stringify(res.city));

            this.sendUser();

        }, error => {
            this.sendUser();
        });
    }

    sendUser() {
        const headers = {
            headers: new HttpHeaders({
                userIP: `${ sessionStorage.getItem('userIP') || undefined }`,
                userCountry: `${ sessionStorage.getItem('userCountry') || undefined }`,
                userCity: `${ sessionStorage.getItem('userCity') || undefined }`,
                user: `${ this.userDataService.getUserName() || undefined }`,
                userId: `${ this.getSessionCookie() || undefined }`
            })
        };

        this.http.post<any>(this.USER_STATISTICS_URL, {responseType: 'text'}, headers).subscribe(data => {
            if (data.sessionId !== "true") {
                this.setSessionCookie(data.sessionId)
            }
        }, error => {
            console.log(error)
        });


    }

    userOut() {
        const date = formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss', 'en_US')
        const userIPOptions = {
            headers: new HttpHeaders({
                user: `${ this.userDataService.getUserName() || undefined }`,
                userIP: `${ sessionStorage.getItem('userIP') || undefined }`,
                userId: `${ this.getSessionCookie() || undefined }`,
                dateOut: `${ date }`,
            })
        };
        return this.http.post(this.USER_OUT_STATISTICS_URL, {}, userIPOptions);
    }

    public findAll(fromDate, toDate, pageSize, pageIndex): Observable<VisitStatisticGraph> {
        return this.http.get<VisitStatisticGraph>(`${ this.GRAPH_STATISTICS_URL }/fromDate/${ fromDate }` +
            `/toDate/${ toDate }/pageSize/${ pageSize }/page/${ pageIndex }`);
    }

    setSessionCookie(sessionId) {
        this.cookies.set(this.SESSION_USER_ID_COOKIE, sessionId, {
            expires: moment().add(10, 'years').toDate(),
            path: '/',
        });
    }

    getSessionCookie() {
        return this.cookies.get(this.SESSION_USER_ID_COOKIE);
    }
}
