import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProxyList } from "../models/proxyList";

@Injectable({
    providedIn: 'root'
})
export class ProxyService {

    private readonly PROXY_URL: string;

    constructor(private http: HttpClient) {
        this.PROXY_URL = environment.apiUrl + '/proxy';
    }

    public findAll(pageSize, page): Observable<ProxyList> {
        return this.http.get<ProxyList>(this.PROXY_URL + `/list` + '/' + pageSize + '/' + page);
    }


}
