import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ItemsService } from '../../service/items.service';

@Component({
  selector: 'app-edit-items',
  templateUrl: './edit-items.component.html',
  styleUrls: ['./edit-items.component.css']
})
export class EditItemsComponent implements OnInit {
  itemForm: FormGroup;
  itemsId: string;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private itemsService: ItemsService
  ) {
    this.itemForm = this.fb.group({
      itemName: ['', Validators.required],
      stock: ['', [Validators.required, Validators.pattern('^[0-9]*$'), Validators.min(1)]],
      price: ['', [Validators.required, Validators.pattern('^[0-9]*$'), Validators.min(1)]]
    });

    this.itemsId = this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.loadItemData();
  }

  loadItemData(): void {
    this.itemsService.getItemById(this.itemsId).subscribe(
      response => {
        const itemData = response.data;
        this.itemForm.patchValue({
          itemName: itemData.itemsName,
          stock: itemData.stock,
          price: itemData.price
        });
      },
      error => {
        console.error('Error fetching item data', error);
      }
    );
  }

  onSubmit(): void {
    if (this.itemForm.valid) {
      const itemData = {
        itemsName: this.itemForm.get('itemName')!.value,
        stock: this.itemForm.get('stock')!.value,
        price: this.itemForm.get('price')!.value
      };

      this.itemsService.updateItem(this.itemsId, itemData).subscribe(
        response => {
          console.log('Item updated:', response);
          this.router.navigate(['/items']);
        },
        error => {
          console.error('Error updating item:', error);
        }
      );
    }
  }
}
