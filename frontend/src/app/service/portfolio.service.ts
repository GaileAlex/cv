import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Portfolio} from "../models/portfolio";

@Injectable({
  providedIn: 'root'
})
export class PortfolioService {

  private portfolioUrl: string;


  constructor(private http: HttpClient) {
    this.portfolioUrl = 'http://localhost:8080/mindly-data';
  }

  public findAll(): Observable<Portfolio[]> {

    return this.http.get<Portfolio[]>(this.portfolioUrl);
  }

  public save(portfolio: Portfolio) {
    return this.http.post<Portfolio>(this.portfolioUrl, portfolio);
  }

  public deletePortfolio(portfolio) {
    return this.http.delete(this.portfolioUrl + "/" + portfolio.id);
  }

}
