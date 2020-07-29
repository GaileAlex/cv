import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from "../../environments/environment";
import { Blog } from "../models/blog";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class BlogService {

    private readonly booksUrl: string;

    constructor(private http: HttpClient) {
        this.booksUrl = environment.apiUrl + '/blog';
    }

    public findAll(): Observable<Blog[]> {
        return this.http.get<Blog[]>(this.booksUrl);
    }

    public findBlogById(blogId): Observable<Blog> {
        return this.http.get<Blog>(this.booksUrl + '/find-blog/' + blogId);
    }

}
