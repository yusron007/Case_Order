import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { OrderService } from '../service/order.service';

interface Order {
  orderId: number;
  customerName: string;
  itemsName: string;
  orderCode: string;
  quantity: number;
  totalPrice: number;
  orderDate: Date;
}

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit, AfterViewInit {

  orders: Order[] = [];
  displayedColumns: string[] = ['customerName', 'itemsName', 'orderCode', 'quantity', 'totalPrice', 'orderDate', 'action'];
  dataSource = new MatTableDataSource<Order>();

  searchKey: string = '';
  totalElements = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;


  constructor(private router: Router, private orderService: OrderService) { }

  ngOnInit() {
    this.fetchOrders({ pageIndex: 0, pageSize: 10, length: 0 });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  fetchOrders(event: PageEvent) {
    const page = event.pageIndex;
    const size = event.pageSize;

    this.orderService.getOrder(page, size).subscribe(response => {
      if (response && response.data) {
        this.orders = response.data.map((order: any) => ({
          orderId: order.orderId,
          customerName: order.customer.customerName,
          itemsName: order.items.itemsName,
          orderCode: order.orderCode,
          quantity: order.quantity,
          totalPrice: order.totalPrice,
          orderDate: new Date(order.orderDate)
        }));
        this.dataSource.data = this.orders;
        this.totalElements = response.pagination.totalElements;
      }
    });
  }

  applyFilter(): void {
    this.dataSource.filter = this.searchKey.trim().toLowerCase();
    this.dataSource.filterPredicate = (data: Order, filter: string) => data.customerName.toLowerCase().includes(filter);
  }

  navigateToAddOrder(): void {
    this.router.navigate(['order/add-order']);
  }

  editOrder(orderId: number): void {
    this.router.navigate(['order/edit-order', orderId]);
  }

  deleteOrder(orderId: number): void {
    this.orderService.deleteOrder(orderId).subscribe(
      response => {
        console.log('Order deleted:', response);
        this.dataSource.data = this.dataSource.data.filter(order => order.orderId !== orderId);
      },
      error => {
        console.error('Error deleting Order:', error);
      }
    );
  }

  viewOrder(orderId: number): void {
    this.router.navigate(['order/detail-order', orderId]);
  }
}
