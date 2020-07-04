import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Books } from '../models/books';
import { Constants } from '../constants/appConstants';

@Injectable({
    providedIn: 'root'
})
export class BooksService {

    private readonly booksUrl: string;

    constructor(private http: HttpClient) {
        this.booksUrl = Constants.API_V1_PREFIX + '/librarian';
    }

    public findAll(): Observable<Books[]> {
        return this.http.get<Books[]>(this.booksUrl + '/find-all');
    }

    public applyFilter(filters, condition) {
        return this.http.post<Books[]>(`${ this.booksUrl }/${ condition }`, filters);
    }
}
