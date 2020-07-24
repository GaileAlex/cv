import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private auth: AuthService, private router: Router) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if ([401, 403].includes(err.status) && this.auth.getUserName()) {
                // auto logout if 401 or 403 response returned from api
                this.auth.logout();
            }
            let error = (err && err.error && err.error.message) || err.statusText;
            if([0].includes(err.status)){
              error='Server not responding'
            }
            return throwError(error);
        }));
    }
}
