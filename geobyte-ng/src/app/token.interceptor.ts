import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {LoginResponse} from './auth/auth.model';
import {catchError, switchMap} from 'rxjs/operators';
import {AuthService} from './auth/shared/auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  isTokenRefreshing = false;
  refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);

  constructor(
    private authService: AuthService
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (this.isHeaderNeeded(request.url)) {
      request = this.addToken(request, this.authService.getJwtToken());
    }

    return next.handle(request).pipe(catchError(err => {
      if (err instanceof HttpErrorResponse && err.status === 403) {
        return this.handleAuthErrors(request, next);
      } else {
        return throwError(err);
      }
    }));
  }

  addToken(req: HttpRequest<any>, jwtToken: any) {
    return req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + jwtToken)
    });
  }

  isHeaderNeeded(url: string){
    return !url.includes('auth');
  }

  handleAuthErrors(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken()
        .pipe(switchMap((refreshTokenResponse: LoginResponse) => {
          this.isTokenRefreshing = false;
          this.refreshTokenSubject
            .next(refreshTokenResponse.token);
          return next.handle(this.addToken(req, refreshTokenResponse.token));
        }));
    }
  }
}
