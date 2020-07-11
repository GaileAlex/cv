import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Books } from '../models/books';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class BooksService {

    private readonly booksUrl: string;

    constructor(private http: HttpClient) {
        this.booksUrl = environment.apiUrl + '/librarian';
    }

    public findAll(): Observable<Books[]> {
        return this.http.get<Books[]>(this.booksUrl + '/find-all');
    }

    public applyFilter(filters, condition): Observable<Books[]> {
        return this.http.post<Books[]>(this.booksUrl + '/' + condition, filters);
    }
}
