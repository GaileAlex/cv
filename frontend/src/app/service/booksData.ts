import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class BooksDataService {
    public booksData: any;

    public constructor() {
    }
}
