import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {Constants} from '../../constants/appConstants';
import {User} from "../../models/user";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private token_type: string = 'Bearer';

  constructor(private http: HttpClient, private router: Router) {
  }

  register(user): Observable<any> {
    return this.http.post(Constants.REGISTRATION_URL, {
      username: user.username,
      email: user.email,
      password: user.password
    }, httpOptions);
  }

  login(username, password): Observable<any> {
    return this.http.post(Constants.LOGIN_URL, {
      username: username,
      password: password
    }, httpOptions);
  }

  logout(){

  }

  getUserName(): string {
    const userString = sessionStorage.getItem('user');
    let userData = new User();
    if (userString) {
      const user: User = JSON.parse(userString);
      userData.username = user.username;

    }
    return userData.username;
  }

  isAuthenticated(): boolean {
    return !!sessionStorage.getItem('accessToken') && !!sessionStorage.getItem('user');
  }

  isTokenPresent(): boolean {
    return !!sessionStorage.getItem('accessToken');
  }
}
