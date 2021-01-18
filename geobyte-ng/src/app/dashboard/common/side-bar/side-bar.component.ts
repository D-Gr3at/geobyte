import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../../auth/shared/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  logout(){
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
