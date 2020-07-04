import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Mindly} from '../models/mindly';
import {Constants} from '../constants/appConstants';

@Injectable({
    providedIn: 'root'
})
export class PortfolioService {

    private readonly portfolioUrl: string;
    private readonly deletePortfolioUrl: string;

    constructor(private http: HttpClient) {
        this.portfolioUrl = Constants.API_V1_PREFIX + '/mindly-data';
        this.deletePortfolioUrl = this.portfolioUrl + '/delete';
    }

    public findAll(): Observable<Mindly[]> {

        return this.http.get<Mindly[]>(this.portfolioUrl);
    }

    public save(portfolio: Mindly) {
        return this.http.post<Mindly>(this.portfolioUrl, portfolio);
    }

    public deletePortfolio(portfolioId) {
        return this.http.post<string>(`${this.deletePortfolioUrl}/${portfolioId}`, null);
    }
}
