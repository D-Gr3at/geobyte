import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AddLocationComponent } from './add-location/add-location.component';
import { EditLocationComponent } from './edit-location/edit-location.component';
import { LocationListComponent } from './location-list/location-list.component';
import { SideBarComponent } from './common/side-bar/side-bar.component';


@NgModule({
  declarations: [
    DashboardComponent,
    AddLocationComponent,
    EditLocationComponent,
    LocationListComponent,
    SideBarComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule
  ]
})
export class DashboardModule { }
