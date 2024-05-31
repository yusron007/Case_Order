import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderService } from '../../service/order.service';

@Component({
  selector: 'app-add-order',
  templateUrl: './add-order.component.html',
  styleUrl: './add-order.component.css'
})
export class AddOrderComponent implements OnInit {
  orderForm: FormGroup;
  items: any[] = [];
  customers: any[] = [];

  constructor(
    private fb: FormBuilder,
    private orderService: OrderService,
    private router: Router
  ) {
    this.orderForm = this.fb.group({
      customerId: ['', Validators.required],
      itemsId: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.pattern('^[0-9]*$'), Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.fetchItems();
    this.fetchCustomers();
  }

  fetchItems(): void {
    this.orderService.getNameItems().subscribe(response => {
      this.items = response.data;
    });
  }

  fetchCustomers(): void {
    this.orderService.getNameCustomer().subscribe(response => {
      this.customers = response.data;
    });
  }

  onSubmit(): void {
    if (this.orderForm.valid) {
      const orderData = {
        customerId: this.orderForm.get('customerId')?.value,
        itemsId: this.orderForm.get('itemsId')?.value,
        quantity: this.orderForm.get('quantity')?.value
      };

      this.orderService.createOrder(orderData).subscribe(
        response => {
          console.log('Order added successfully', response);
          this.router.navigate(['/order']);
        },
        error => {
          console.error('Error adding order', error);
        }
      );
    }
  }
}
