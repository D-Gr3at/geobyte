import {Component, OnInit} from '@angular/core';
import {AuthService} from '../auth/shared/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLoggedIn = false;
  openSideBar = true;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isAuthenticated();
  }

  logout() {
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }

  expandSideBar() {
    console.log(this.toggleSideBar());
  }

  toggleSideBar() {
    return this.openSideBar = !this.openSideBar;
  }

}
