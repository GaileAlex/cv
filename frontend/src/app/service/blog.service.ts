import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Constants } from '../constants/appConstants';
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class BlogService {

    private readonly booksUrl: string;
    blog: string;

    constructor(private http: HttpClient) {
        this.booksUrl = environment.apiUrl + '/blog';
    }

    public findAll(): any {
        return this.http.get(this.booksUrl, {responseType: 'text'}).subscribe(result => {
            this.blog = result;
        }, error => console.log(error));
    }
}
