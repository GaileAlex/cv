import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor() {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add auth header with jwt if user is logged in and request is to the api url
        sessionStorage.getItem('roles');
        const user = sessionStorage.getItem('user');
        const isLoggedIn = user && sessionStorage.getItem('accessToken');
        const isApiUrl = request.url.startsWith(environment.apiUrl);
        if (isLoggedIn && isApiUrl) {
            request = request.clone({
                setHeaders: {Authorization: `Bearer ${ sessionStorage.getItem('accessToken') }`}
            });
        }
        return next.handle(request);
    }
}
