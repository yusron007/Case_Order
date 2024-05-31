import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddCustomerComponent } from './customer/add-customer/add-customer.component';
import { CustomerComponent } from './customer/customer.component';
import { DetailCustomerComponent } from './customer/detail-customer/detail-customer.component';
import { EditCustomerComponent } from './customer/edit-customer/edit-customer.component';
import { AddItemsComponent } from './items/add-items/add-items.component';
import { DetailItemsComponent } from './items/detail-items/detail-items.component';
import { EditItemsComponent } from './items/edit-items/edit-items.component';
import { ItemsComponent } from './items/items.component';
import { AddOrderComponent } from './order/add-order/add-order.component';
import { DetailOrderComponent } from './order/detail-order/detail-order.component';
import { EditOrderComponent } from './order/edit-order/edit-order.component';
import { OrderComponent } from './order/order.component';

export const routes: Routes = [
  { path: '', redirectTo: '/customer', pathMatch: 'full' },
  { path: 'customer', component: CustomerComponent},
  { path: 'customer/add-customer', component: AddCustomerComponent },
  { path: 'customer/edit-customer/:id', component: EditCustomerComponent },
  { path: 'customer/detail-customer/:id', component: DetailCustomerComponent},
  { path: 'items', component: ItemsComponent},
  { path: 'items/add-items', component: AddItemsComponent },
  { path: 'items/edit-items/:id', component: EditItemsComponent },
  { path: 'items/detail-items/:id', component: DetailItemsComponent},
  { path: 'order', component: OrderComponent},
  { path: 'order/add-order', component: AddOrderComponent },
  { path: 'order/edit-order/:id', component: EditOrderComponent },
  { path: 'order/detail-order/:id', component: DetailOrderComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }

