import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Books} from "../models/books";

@Injectable({
  providedIn: 'root'
})
export class BooksService {

  private readonly booksUrl: string;

  constructor(private http: HttpClient) {
    this.booksUrl = 'http://localhost:8080/librarian/';
  }

  public findAll(): Observable<Books[]> {

    return this.http.get<Books[]>(this.booksUrl);
  }

  public save(filters, condition) {

    return this.http.post<any>(`${this.booksUrl}/${condition}`, filters)
  }
}
