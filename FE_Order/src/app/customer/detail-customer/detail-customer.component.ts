import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../service/customer.service';

@Component({
  selector: 'app-detail-customer',
  templateUrl: './detail-customer.component.html',
  styleUrls: ['./detail-customer.component.css']
})
export class DetailCustomerComponent implements OnInit {
  customer: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService
  ) {
    this.customer = {
      image: '',
      name: '',
      address: '',
      code: '',
      phone: '',
      active: false,
      lastOrderDate: null
    };
  }

  ngOnInit(): void {
    const customerId = this.route.snapshot.paramMap.get('id');
    if (customerId) {
      this.loadCustomerData(customerId);
    }
  }

  loadCustomerData(customerId: string): void {
    this.customerService.getCustomerById(customerId).subscribe(
      response => {
        const data = response.data;
        this.customer = {
          image: data.pic,
          name: data.customerName,
          address: data.customerAddress,
          code: data.customerCode,
          phone: data.customerPhone,
          active: data.isActive,
          lastOrderDate: new Date(data.lastOrderDate)
        };
      },
      error => {
        console.error('Error fetching customer data', error);
      }
    );
  }

  backToMenu(): void {
    this.router.navigate(['/customer']);
  }
}
