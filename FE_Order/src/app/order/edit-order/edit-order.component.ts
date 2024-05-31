import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../service/order.service';

@Component({
  selector: 'app-edit-order',
  templateUrl: './edit-order.component.html',
  styleUrl: './edit-order.component.css'
})

export class EditOrderComponent implements OnInit {

  orderForm: FormGroup;
  orderId: string;
  items: any[] = [];
  customers: any[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) {
    this.orderForm = this.fb.group({
      customerId: ['', Validators.required],
      itemsId: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.pattern('^[0-9]*$'), Validators.min(1)]]
    });

    this.orderId = this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.fetchItems();
    this.fetchCustomers();
    this.loadOrderData();
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

  loadOrderData(): void {
    this.orderService.getOrderById(this.orderId).subscribe(
      response => {
        const orderData = response.data;
        this.orderForm.patchValue({
          customerId: orderData.customer.customerId,
          itemsId: orderData.items.itemsId,
          quantity: orderData.quantity
        });
      },
      error => {
        console.error('Error fetching order data', error);
      }
    );
  }

  onSubmit(): void {
    if (this.orderForm.valid) {
      const orderData = {
        customerId: this.orderForm.get('customerId')!.value,
        itemsId: this.orderForm.get('itemsId')!.value,
        quantity: this.orderForm.get('quantity')!.value
      };

      this.orderService.updateOrder(this.orderId, orderData).subscribe(
        response => {
          console.log('Order updated:', response);
          this.router.navigate(['/order']);
        },
        error => {
          console.error('Error updating order:', error);
        }
      );
    }
  }
}
