import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {passwordValidator} from '../validators/password.validators';
import {SignUpModel} from '../auth.model';
import {AuthService} from '../shared/auth.service';
import {animate, style, transition, trigger} from '@angular/animations';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  animations: [
    trigger('flyInOut', [
      transition(':enter', [
        style({transform: 'translateX(-100%)'}),
        animate('0.8s')
      ])
    ])
  ]
})
export class SignupComponent implements OnInit {

  @ViewChild('responseMessage') responseMessage: ElementRef;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private renderer: Renderer2,
    private router: Router
  ) {
  }

  signUpForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(6)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(8)]],
    terms: ['', Validators.required]
  }, {validators: passwordValidator});

  ngOnInit(): void {
  }

  get name() {
    return this.signUpForm.get('name');
  }

  get email() {
    return this.signUpForm.get('email');
  }

  get password() {
    return this.signUpForm.get('password');
  }

  get confirmPassword() {
    return this.signUpForm.get('confirmPassword');
  }

  get terms() {
    return this.signUpForm.get('terms');
  }

  submit() {
    this.authService.signUp(new SignUpModel(this.name.value, this.email.value, this.password.value))
      .subscribe((response) => {
        console.log(response);
        this.addSuccessMessage();
        setTimeout(() => {
          this.router.navigate(['/login'],
            {queryParams: {registered: true}});
        }, 5000);
      }, error => {
        console.log(error);
        this.failureMessage();
      });
  }

  addSuccessMessage() {
    const HTMLString = `<div class="alert alert-success text-center alert-dismissible fade show" [@flyInOut] role="alert">
          <strong>Registered successfully!</strong> An email has been sent to verify your account
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>`;
    const node = new DOMParser().parseFromString(HTMLString, 'text/html');
    const parentElement = this.renderer.selectRootElement(this.responseMessage.nativeElement, false);
    this.renderer.appendChild(parentElement, node.body);
    this.signUpForm.reset();
  }

  failureMessage(){
    const HTMLString = `<div class="alert alert-danger text-center alert-dismissible fade show" [@flyInOut] role="alert">
          <strong>Failed to register!</strong> Please check you internet connection
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>`;
    const node = new DOMParser().parseFromString(HTMLString, 'text/html');
    const parentElement = this.renderer.selectRootElement(this.responseMessage.nativeElement, false);
    this.renderer.appendChild(parentElement, node.body);
  }

}
