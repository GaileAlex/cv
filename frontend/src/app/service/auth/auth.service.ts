import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {Constants} from '../../constants/appConstants';
import {User} from '../../models/user';
import {environment} from "../../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private TOKEN_TYPE = 'Bearer';

  constructor(private http: HttpClient, private router: Router) {
  }

  register(user): Observable<any> {
    return this.http.post(environment.apiAuthUrl + Constants.REGISTRATION_URL, {
      username: user.username,
      email: user.email,
      password: user.password
    }, httpOptions);
  }

  login(username, password): Observable<any> {
    return this.http.post(environment.apiAuthUrl + Constants.LOGIN_URL, {
      username,
      password
    }, httpOptions);
  }

  logout() {
    sessionStorage.setItem('user', '');
    sessionStorage.setItem('accessToken', '');
    sessionStorage.setItem('roles', '');
    this.router.navigate(['/']);
  }

  getUserName(): string {
    const userString = sessionStorage.getItem('user');
    const userData = new User();
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
