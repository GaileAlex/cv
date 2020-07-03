import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Constants} from "../../constants/appConstants";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor() {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add auth header with jwt if user is logged in and request is to the api url

    sessionStorage.getItem('roles');
    const user = sessionStorage.getItem('user');
    console.log(user)
    const isLoggedIn = user && sessionStorage.getItem('accessToken');
    const isApiUrl = request.url.startsWith(Constants.API_V1_PREFIX);
    if (isLoggedIn && isApiUrl) {
      request = request.clone({
        setHeaders: {Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`}
      });
    }
    return next.handle(request);
  }
}
