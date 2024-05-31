import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routes';
import { AddCustomerComponent } from './customer/add-customer/add-customer.component';
import { CustomerComponent } from './customer/customer.component';
import { DetailCustomerComponent } from './customer/detail-customer/detail-customer.component';
import { EditCustomerComponent } from './customer/edit-customer/edit-customer.component';
import { AddItemsComponent } from './items/add-items/add-items.component';
import { DetailItemsComponent } from './items/detail-items/detail-items.component';
import { EditItemsComponent } from './items/edit-items/edit-items.component';
import { ItemsComponent } from './items/items.component';
import { NavbarComponent } from './navbar/navbar.component';
import { AddOrderComponent } from './order/add-order/add-order.component';
import { DetailOrderComponent } from './order/detail-order/detail-order.component';
import { EditOrderComponent } from './order/edit-order/edit-order.component';
import { OrderComponent } from './order/order.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CustomerComponent,
    ItemsComponent,
    OrderComponent,
    AddCustomerComponent,
    EditCustomerComponent,
    DetailCustomerComponent,
    AddItemsComponent,
    EditItemsComponent,
    DetailItemsComponent,
    AddOrderComponent,
    EditOrderComponent,
    DetailOrderComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    AppRoutingModule,
    FormsModule,
    NgbModule,
    MatTableModule,
    MatInputModule,
    MatIconModule,
    MatCardModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatListModule,
    MatDividerModule,
    MatPaginatorModule,
    MatDialogModule,
    MatSnackBarModule,
    MatAutocompleteModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
