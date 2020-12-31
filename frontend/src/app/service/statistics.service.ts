import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { UserDataService } from './user-data.service';
import { Observable } from 'rxjs';
import { VisitStatisticGraph } from '../models/visitStatisticGraph';
import { formatDate } from "@angular/common";

@Injectable({
    providedIn: 'root'
})
export class StatisticsService {
    private readonly USER_STATISTICS_URL: string;
    private readonly GRAPH_STATISTICS_URL: string;
    private readonly USER_OUT_STATISTICS_URL: string;

    constructor(private http: HttpClient, private userDataService: UserDataService) {
        this.USER_STATISTICS_URL = environment.apiUrl + '/statistic/user';
        this.USER_OUT_STATISTICS_URL = environment.apiUrl + '/statistic/user-out';
        this.GRAPH_STATISTICS_URL = environment.apiUrl + '/statistic/graph';
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
        return this.http.post(this.USER_STATISTICS_URL, {}, userIPOptions);
    }

    userOut() {
        const date = formatDate(new Date(), 'yyyy-MM-dd H:mm:ss', 'en_US')

        const userIPOptions = {
            headers: new HttpHeaders({
                userIP: `${ sessionStorage.getItem('userIP') }`,
                dateOut: `${ date }`,
            })
        };
        return this.http.post(this.USER_OUT_STATISTICS_URL, {}, userIPOptions);
    }

    public findAll(fromDate, toDate): Observable<VisitStatisticGraph> {
        return this.http.get<VisitStatisticGraph>(`${ this.GRAPH_STATISTICS_URL }/${ fromDate }/${ toDate }`);
    }
}
