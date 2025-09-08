import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";

interface GenerateRequest {
    model: string;
    prompt: string;
    stream: boolean;
}

interface GenerateResponse {
    response: string;
}

@Injectable({
    providedIn: 'root'
})
export class ChatService {

    private readonly apiUrl: string;

    constructor(private http: HttpClient) {
        this.apiUrl = environment.apiUrl;
    }

    generate(request: GenerateRequest): Observable<GenerateResponse> {
        console.log("request - " +request);
        console.log(request);
        return this.http.post<GenerateResponse>(this.apiUrl + '/api/chat/generate', request);
    }

}
