import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LoginResponse, LoginRequestModel, SignUpModel} from '../auth.model';
import {environment} from '../../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {map, tap} from 'rxjs/operators';
import {LocalStorageService} from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  env = environment;

  constructor(
    private httpClient: HttpClient,
    private localStorage: LocalStorageService
  ) {
  }

  signUp(signUp: SignUpModel): Observable<any> {
    return this.httpClient.post(this.env.baseUrl + '/auth/signup', signUp, {responseType: 'text'});
  }

  login(loginData: LoginRequestModel): Observable<boolean> {
    return this.httpClient.post<LoginResponse>(this.env.baseUrl + '/auth/login', loginData)
      .pipe(map(response => {
        this.localStorage.store('token', response.token);
        this.localStorage.store('email', response.email);
        this.localStorage.store('expiresAt', response.expiresAt);
        this.localStorage.store('refreshToken', response.refreshToken);
        return true;
      }));
  }

  getJwtToken() {
    return this.localStorage.retrieve('token');
  }

  getRefreshToken() {
    return this.localStorage.retrieve('refreshToken');
  }

  getUsername() {
    return this.localStorage.retrieve('username');
  }

  isAuthenticated() {
    return this.getJwtToken() !== null;
  }

  refreshToken(): Observable<LoginResponse> {
    const refreshTokenPayload = {
      refreshToken: this.getRefreshToken(),
      username: this.getUsername()
    };
    return this.httpClient.post<LoginResponse>(this.env.baseUrl + '/auth/refresh/token', refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.store('token', response.token);
        this.localStorage.store('expiresAt', response.expiresAt);
      }));
  }

  logout() {
    this.httpClient.post(this.env.baseUrl + '/logout', null, {responseType: 'json'});
    this.localStorage.clear('token');
    this.localStorage.clear('email');
    this.localStorage.clear('expiresAt');
    this.localStorage.clear('refreshToken');
  }
}
