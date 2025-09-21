import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ChatService {

    private readonly apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) {
    }

    sendMessage(userName: string, message: string): Observable<string> {
        return this.http.post(
            `${ this.apiUrl }/api/chat/${ userName }`,
            {message},
            {responseType: 'text'}    // 👈 ключевой момент
        );
    }

    getHistory(userName: string): Observable<any[]> {
        return this.http.get<any[]>(`${ this.apiUrl }/api/chat/${ userName }/history`);
    }
}
