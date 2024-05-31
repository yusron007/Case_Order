import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { CustomerService } from '../service/customer.service';

interface Customer {
  id: string;
  image: string;
  name: string;
  address: string;
  code: string;
  phone: string;
  active: boolean;
  lastOrderDate: Date;
}

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})

export class CustomerComponent implements OnInit, AfterViewInit {

  customers: Customer[] = [];
  displayedColumns: string[] = ['image', 'name', 'address', 'code', 'phone', 'active', 'lastOrderDate', 'action'];
  dataSource = new MatTableDataSource<Customer>();

  searchKey: string = '';
  totalElements = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private router: Router, private customerService: CustomerService) { }

  ngOnInit() {
    this.fetchCustomers({ pageIndex: 0, pageSize: 10, length: 0 });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  fetchCustomers(event: PageEvent) {
    const page = event.pageIndex;
    const size = event.pageSize;

    this.customerService.getCustomers(page, size).subscribe(response => {
      if (response && response.data) {
        this.customers = response.data.map((item: any) => ({
          id: item.customerId,
          image: item.pic,
          name: item.customerName,
          address: item.customerAddress,
          code: item.customerCode,
          phone: item.customerPhone,
          active: item.isActive,
          lastOrderDate: new Date(item.lastOrderDate)
        }));
        this.dataSource.data = this.customers;
        this.totalElements = response.pagination.totalElements;
      }
    });
  }

  applyFilter(): void {
    this.dataSource.filter = this.searchKey.trim().toLowerCase();
    this.dataSource.filterPredicate = (data: Customer, filter: string) => data.name.toLowerCase().includes(filter);
  }

  navigateToAddCustomer(): void {
    this.router.navigate(['customer/add-customer']);
  }

  editCustomer(customerId: string): void {
    this.router.navigate(['customer/edit-customer', customerId]);
  }

  deleteCustomer(customerId: string): void {
    this.customerService.deleteCustomer(customerId).subscribe(
      response => {
        console.log('Customer deleted:', response);
        this.dataSource.data = this.dataSource.data.filter(customer => customer.id !== customerId);
      },
      error => {
        console.error('Error deleting customer:', error);
      }
    );
  }

  viewCustomer(customerId: string): void {
    this.router.navigate(['customer/detail-customer', customerId]);
  }
}
