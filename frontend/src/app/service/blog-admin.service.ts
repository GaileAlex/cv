import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Blog } from '../models/blog';

@Injectable({
    providedIn: 'root'
})
export class BlogAdminService {

    private readonly blogAdminUrl: string;

    constructor(private http: HttpClient) {
        this.blogAdminUrl = environment.apiUrl + '/admin-blog';
    }

    public findAll(): Observable<Blog> {

        return this.http.get<Blog>(this.blogAdminUrl);
    }

    public save(blog: FormData) {
        return this.http.post<Blog>(this.blogAdminUrl, blog);
    }

    public deletePortfolio(blogId): Observable<Blog> {
        return this.http.delete<Blog>(`${ this.blogAdminUrl }/${ blogId }`);
    }
}
