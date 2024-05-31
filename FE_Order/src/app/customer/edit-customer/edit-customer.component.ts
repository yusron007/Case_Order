import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../service/customer.service';

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css']
})
export class EditCustomerComponent implements OnInit {
  customerForm: FormGroup;
  customerId: string;
  fileToUpload: File | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService
  ) {
    this.customerForm = this.fb.group({
      image: [''],
      name: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern('^((\\+91-?)|0)?[0-9]{10,15}$')]]
    });

    this.customerId = this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.loadCustomerData();
  }

  loadCustomerData(): void {
    this.customerService.getCustomerById(this.customerId).subscribe(
      response => {
        const customerData = response.data;
        this.customerForm.patchValue({
          name: customerData.customerName,
          address: customerData.customerAddress,
          phone: customerData.customerPhone
        });
      },
      error => {
        console.error('Error fetching customer data', error);
      }
    );
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.fileToUpload = file;
      this.customerForm.patchValue({ image: file.name }); // Display file name in form
    }
  }

  onSubmit(): void {
    if (this.customerForm.valid) {
      const formData = new FormData();
      formData.append('customerName', this.customerForm.get('name')!.value);
      formData.append('customerAddress', this.customerForm.get('address')!.value);
      formData.append('customerPhone', this.customerForm.get('phone')!.value);
      if (this.fileToUpload) {
        formData.append('pic', this.fileToUpload);
      } else {
        console.log("error")
      }

      this.customerService.updateCustomer(this.customerId, formData).subscribe(
        response => {
          console.log('Customer updated:', response);
          this.router.navigate(['/customer']);
        },
        error => {
          console.error('Error updating customer:', error);
        }
      );
    }
  }
}
