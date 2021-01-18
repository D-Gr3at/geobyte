import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {EditLocationComponent} from './edit-location/edit-location.component';
import {LocationListComponent} from './location-list/location-list.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {AddLocationComponent} from './add-location/add-location.component';


const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children: [
      {path: 'welcome', component: AddLocationComponent},
      {path: 'locations', component: LocationListComponent},
      {path: 'edit-location/:id', component: EditLocationComponent},
      {path: 'edit-location/:id', component: EditLocationComponent}
    ],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
