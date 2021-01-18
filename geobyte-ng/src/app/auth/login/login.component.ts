import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../shared/auth.service';
import {LoginRequestModel} from '../auth.model';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, AfterViewInit {

  loginForm: FormGroup;

  @ViewChild('responseMessage') responseMessage: ElementRef;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private renderer: Renderer2,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.minLength(6)]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  ngAfterViewInit(): void {
    this.activatedRoute.queryParams
      .subscribe(params => {
        if (params.registered !== undefined && Object.entries(params.registered).length !== 0 && params.registered === 'true') {
          this.info();
        }
      });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  submit() {
    this.authService.login(new LoginRequestModel(this.email.value, this.password.value))
      .subscribe(response => {
        if (response) {
          this.router.navigate(['/welcome']).then(r => {
            if (r){
              location.reload();
            }
          });
        }
      }, error => {
        console.log(error);
        this.error(error.error.message);
      });
  }

  info() {
    const HTMLString = `<div class="alert alert-info text-center alert-dismissible fade show" [@flyInOut] role="alert">
          <strong>Welcome!</strong> Please verify your account before you can login
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>`;
    const node = new DOMParser().parseFromString(HTMLString, 'text/html');
    const parentElement = this.renderer.selectRootElement(this.responseMessage.nativeElement, false);
    this.renderer.appendChild(parentElement, node.body);
  }

  error(message) {
    const HTMLString = `<div class="alert alert-danger text-center alert-dismissible fade show" [@flyInOut] role="alert">
          <strong>Failed to Login!</strong> ${message} .
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>`;
    const node = new DOMParser().parseFromString(HTMLString, 'text/html');
    const parentElement = this.renderer.selectRootElement(this.responseMessage.nativeElement, false);
    this.renderer.appendChild(parentElement, node.body);
  }

}
