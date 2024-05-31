import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerService } from '../../service/customer.service';

@Component({
  selector: 'app-add-customer',
  templateUrl: './add-customer.component.html',
  styleUrls: ['./add-customer.component.css']
})
export class AddCustomerComponent implements OnInit {
  customerForm: FormGroup;
  selectedFile: File | null = null;

  constructor(private fb: FormBuilder, private customerService: CustomerService, private router: Router) {
    this.customerForm = this.fb.group({
      image: ['', Validators.required],
      name: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10,15}$/)]]
    });
  }

  ngOnInit(): void { }

  onSubmit(): void {
    if (this.customerForm.valid && this.selectedFile) {
      const formData = new FormData();
      formData.append('customerName', this.customerForm.get('name')?.value);
      formData.append('customerAddress', this.customerForm.get('address')?.value);
      formData.append('customerPhone', this.customerForm.get('phone')?.value);
      formData.append('pic', this.selectedFile);

      this.customerService.createCustomer(formData).subscribe(
        response => {
          console.log('Customer added successfully', response);
          this.router.navigate(['/customer']);
        },
        error => {
          console.error('Error adding customer', error);
        }
      );
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.customerForm.patchValue({ image: file.name });
    }
  }
}
