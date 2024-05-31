import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../service/order.service';

@Component({
  selector: 'app-detail-order',
  templateUrl: './detail-order.component.html',
  styleUrl: './detail-order.component.css'
})
export class DetailOrderComponent implements OnInit {
  order: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) {
    this.order = {
      customer: {
        customerName: '',
        customerAddress: '',
        customerPhone: ''
      },
      items: {
        itemsName: '',
        itemsCode: '',
        stock: 0,
        price: 0
      },
      orderCode: '',
      quantity: 0,
      totalPrice: 0,
      orderDate: ''
    };
  }

  ngOnInit(): void {
    const orderId = this.route.snapshot.paramMap.get('id');
    if (orderId) {
      this.loadOrderData(orderId);
    }
  }

  loadOrderData(orderId: string): void {
    this.orderService.getOrderById(orderId).subscribe(
      response => {
        const data = response.data;
        this.order = {
          customer: {
            customerName: data.customer.customerName,
            customerAddress: data.customer.customerAddress,
            customerPhone: data.customer.customerPhone
          },
          items: {
            itemsName: data.items.itemsName,
            itemsCode: data.items.itemsCode,
            stock: data.items.stock,
            price: data.items.price
          },
          orderCode: data.orderCode,
          quantity: data.quantity,
          totalPrice: data.totalPrice,
          orderDate: new Date(data.orderDate).toLocaleString()
        };
      },
      error => {
        console.error('Error fetching order data', error);
      }
    );
  }

  backToMenu(): void {
    this.router.navigate(['/order']);
  }
}
