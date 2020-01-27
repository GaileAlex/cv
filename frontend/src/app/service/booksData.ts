import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Books} from "../models/books";

@Injectable({
  providedIn: 'root'
})
export class BooksDataService {
  public booksData: any;

  public constructor() {
  }
}
