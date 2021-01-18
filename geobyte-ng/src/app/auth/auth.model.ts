export class SignUpModel {

  // tslint:disable-next-line:variable-name
  constructor(private name: string, private email: string, private password: string) {
  }

  get _username() {
    return this.name;
  }

  get _password() {
    return this.password;
  }

  get _email() {
    return this.email;
  }
}

export class LoginRequestModel {
  constructor(private email: string, private password: string) {
  }

  get _username() {
    return this.email;
  }

  get _password() {
    return this.password;
  }
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  expiresAt: string;
  email: string;
}
