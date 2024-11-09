import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EnglishSentence } from "../models/englishSentence";

@Injectable({
    providedIn: 'root'
})
export class EnglishService {

    private readonly English_URL: string;

    constructor(private http: HttpClient) {
        this.English_URL = environment.apiUrl + '/english';
    }

    public findOne(): Observable<EnglishSentence> {
        return this.http.get<EnglishSentence>(this.English_URL);
    }

}
