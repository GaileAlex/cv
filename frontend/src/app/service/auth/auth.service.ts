import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {LoginDetails} from "./login-details";
import {Utils} from "./utils";
import {PersonDetails} from "./person-details";
import {AuthUserResponse} from "./auth-user-response.model";
import {UserData} from "./user-data.model";

import {Constants} from '../../constants/appConstants';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private token_type: string = 'Bearer';

  private userEndpoint: string = '/api/v1/users';
  private loginEndpoint: string = '/oauth/login';
  private loginDetailsEndpoint: string = '/oauth/login-details';

  appRouteChange: Subscription;

  constructor(private http: HttpClient, private router: Router) {
  }

  register(user): Observable<any> {
    return this.http.post(Constants.REGISTRATION_URL, {
      username: user.username,
      email: user.email,
      password: user.password
    }, httpOptions);
  }

  login(username, password ): Observable<any> {
    return   this.http.post(Constants.LOGIN_URL, {
      username: username,
      password: password
    }, httpOptions);
  }

  private getUser(): Observable<AuthUserResponse> {
    return this.http.get<AuthUserResponse>(this.userEndpoint + '/user');
  }

  setAppRouteChange(s: Subscription): void {
    this.appRouteChange = s;
  }

  unsubscribeFromRouteChange(): void {
    if (this.appRouteChange) this.appRouteChange.unsubscribe();
  }

  clearSessionStorage() {
    sessionStorage.setItem('access_token', '');
    sessionStorage.setItem('id_token_hint', '');
    sessionStorage.setItem('logout_endpoint_uri', '');
    sessionStorage.setItem('post_logout_redirect_uri', '');
    sessionStorage.setItem('user', '');
    sessionStorage.setItem('representation', '');
    sessionStorage.setItem('contact_data', '');
  }

  getPersonDetails() {
    return this.http.get<PersonDetails>(this.userEndpoint + '/person-details/self');
  }

  getAuthorizationHeader(): string {
    return this.token_type + ' ' + sessionStorage.getItem('access_token');
  }

  setAuthorizationUrlParams(url: string) {
    return Utils.compileURL(url, {
      access_token: sessionStorage.getItem('access_token')
    });
  }

  process() {
    const isDevLogin = Utils.parseQuery(window.location.search).id_token_hint === 'dev';

    if (isDevLogin) {
      const query = Utils.parseQuery(window.location.search);
      this.setLoginDetailsSessionStorage(query);
      this.initialiseUser();

    } else {
      this.http.get<LoginDetails>(this.loginDetailsEndpoint).subscribe((res: LoginDetails) => {
        if (!Utils.isEmptyObject(res)) {
          this.setLoginDetailsSessionStorage(res);
          this.initialiseUser();
        } else {
          this.router.navigate(['/']);
        }
      }, () => {
        this.clearSessionStorage();
        this.router.navigate(['/']);
      });
    }
  }

  setLoginDetailsSessionStorage(source) {
    sessionStorage.setItem('access_token', source.access_token);
    sessionStorage.setItem('id_token_hint', source.id_token_hint);
    sessionStorage.setItem('logout_endpoint_uri', source.logout_endpoint_uri);

    if (source.post_logout_redirect_uri) {
      sessionStorage.setItem('post_logout_redirect_uri', source.post_logout_redirect_uri);
    }
  }

  initialiseUser() {
    this.getUser().subscribe((res: AuthUserResponse) => {
      sessionStorage.setItem('user', JSON.stringify(res));
      this.router.navigate(['/position']);
    }, () => {
      this.clearSessionStorage();
      this.router.navigate(['/']);
    });
  }



  performLogout() {
    let logoutUri = sessionStorage.getItem('logout_endpoint_uri');
    logoutUri += '?id_token_hint=' + sessionStorage.getItem('id_token_hint');
    const postLogoutRedirect = sessionStorage.getItem('post_logout_redirect_uri');
    if (postLogoutRedirect) {
      logoutUri += '&post_logout_redirect_uri=' + postLogoutRedirect;
    }
    this.clearSessionStorage();
    this.unsubscribeFromRouteChange();
    setTimeout(() => {
      window.location.href = logoutUri;
    }, 1000);
  }

  navigateToLogout() {
    this.unsubscribeFromRouteChange();
    this.router.navigate(['logout']);
  }

  getUserData(): UserData {
    const userString = sessionStorage.getItem('user');
    let userData = new UserData();
    if (userString) {
      const user: AuthUserResponse = JSON.parse(userString);
      userData.firstName = user.firstName;
      userData.lastName = user.lastName;
      userData.name = [user.firstName, user.lastName].join(' ');
      userData.id = user.id;
      userData.identity = user.identity;
      userData.code = user.identityIdentifier;
      userData.type = user.identityType;
      userData.country = user.identityCountry;
    }
    return userData;
  }

  isAuthenticated(): boolean {
    return !!sessionStorage.getItem('access_token') && !!sessionStorage.getItem('user');
  }

  isTokenPresent(): boolean {
    return !!sessionStorage.getItem('access_token');
  }
}
