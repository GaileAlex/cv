import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { UserDataService } from "./user-data.service";
import { Observable } from "rxjs";
import { Statistic } from "../models/statistic";

@Injectable({
    providedIn: 'root'
})
export class StatisticsService {
    private readonly USER_STATISTICS_URL: string;
    private readonly GRAPH_STATISTICS_URL: string;

    constructor(private http: HttpClient, private userDataService: UserDataService) {
        this.USER_STATISTICS_URL = environment.apiUrl + '/statistic/user';
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

    public findAll(): Observable<Statistic[]> {
        console.log(this.http.get<Statistic[]>(this.GRAPH_STATISTICS_URL))
        return this.http.get<Statistic[]>(this.GRAPH_STATISTICS_URL);
    }
}
