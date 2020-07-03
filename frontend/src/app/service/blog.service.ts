import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Constants} from '../constants/appConstants';

@Injectable({
  providedIn: 'root'
})
export class BlogService {

  private readonly booksUrl: string;
  blog:string

  constructor(private http: HttpClient) {
    this.booksUrl = Constants.API_V1_PREFIX + '/blog';
  }

  public findAll():any {
  return this.http.get(this.booksUrl, {responseType: 'text'}).subscribe(result => {
          console.log(result)
          this.blog= result;
      }, error => console.log(error));
  }
}
