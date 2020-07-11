import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mindly } from '../models/mindly';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class PortfolioService {

    private readonly portfolioUrl: string;

    constructor(private http: HttpClient) {
        this.portfolioUrl = environment.apiUrl + '/mindly-data';
    }

    public findAll(): Observable<Mindly[]> {

        return this.http.get<Mindly[]>(this.portfolioUrl);
    }

    public save(portfolio: Mindly) {
        return this.http.post<Mindly>(this.portfolioUrl, portfolio);
    }

    public deletePortfolio(portfolioId): Observable<Mindly> {
        return this.http.delete<Mindly>(`${ this.portfolioUrl }/${ portfolioId }`);
    }
}
