import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ItemsService } from '../../service/items.service';

@Component({
  selector: 'app-detail-items',
  templateUrl: './detail-items.component.html',
  styleUrls: ['./detail-items.component.css']
})
export class DetailItemsComponent implements OnInit {
  item: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private itemsService: ItemsService
  ) {
    this.item = {
      itemName: '',
      itemCode: '',
      stock: 0,
      price: 0,
      isAvailable: false,
      lastReStock: null
    };
  }

  ngOnInit(): void {
    const itemId = this.route.snapshot.paramMap.get('id');
    if (itemId) {
      this.loadItemData(itemId);
    }
  }

  loadItemData(itemId: string): void {
    this.itemsService.getItemById(itemId).subscribe(
      response => {
        const data = response.data;
        this.item = {
          itemName: data.itemsName,
          itemCode: data.itemsCode,
          stock: data.stock,
          price: data.price,
          isAvailable: data.isAvailable,
          lastReStock: new Date(data.lastReStock)
        };
      },
      error => {
        console.error('Error fetching item data', error);
      }
    );
  }

  backToMenu(): void {
    this.router.navigate(['/items']);
  }
}
