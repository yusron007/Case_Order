import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ItemsService } from '../../service/items.service';

@Component({
  selector: 'app-add-items',
  templateUrl: './add-items.component.html',
  styleUrls: ['./add-items.component.css']
})
export class AddItemsComponent implements OnInit {
  itemForm: FormGroup;

  constructor(private fb: FormBuilder, private itemsService: ItemsService, private router: Router) {
    this.itemForm = this.fb.group({
      itemName: ['', Validators.required],
      stock: ['', [Validators.required, Validators.pattern('^[0-9]*$'), Validators.min(1)]],
      price: ['', [Validators.required, Validators.pattern('^[0-9]*$'), Validators.min(1)]]
    });
  }

  ngOnInit(): void { }

  onSubmit(): void {
    if (this.itemForm.valid) {
      const itemData = {
        itemsName: this.itemForm.get('itemName')?.value,
        stock: this.itemForm.get('stock')?.value,
        price: this.itemForm.get('price')?.value
      };

      this.itemsService.createItem(itemData).subscribe(
        response => {
          console.log('Item added successfully', response);
          this.router.navigate(['/items']);
        },
        error => {
          console.error('Error adding item', error);
        }
      );
    }
  }
}
