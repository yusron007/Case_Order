import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ItemsService } from '../service/items.service';

interface Items {
  id: string;
  name: string;
  code: string;
  stock: string;
  price: string;
  available: boolean;
  lastReStock: Date;
}

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrl: './items.component.css'
})
export class ItemsComponent implements OnInit, AfterViewInit {

  items: Items[] = [];
  displayedColumns: string[] = ['name', 'code', 'stock', 'price', 'available', 'lastReStock', 'action'];
  dataSource = new MatTableDataSource<Items>();

  searchKey: string = '';
  totalElements = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private router: Router, private itemService: ItemsService) { }

  ngOnInit() {
    this.fetchItems({ pageIndex: 0, pageSize: 10, length: 0 });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  fetchItems(event: PageEvent) {
    const page = event.pageIndex;
    const size = event.pageSize;

    this.itemService.getItems(page, size).subscribe(response => {
      if (response && response.data) {
        this.items = response.data.map((item: any) => ({
          id: item.itemsId,
          name: item.itemsName,
          code: item.itemsCode,
          stock: item.stock,
          price: item.price,
          available: item.isAvailable,
          lastReStock: new Date(item.lastReStock)
        }));
        this.dataSource.data = this.items;
        this.totalElements = response.pagination.totalElements;
      }
    });
  }

  applyFilter(): void {
    this.dataSource.filter = this.searchKey.trim().toLowerCase();
    this.dataSource.filterPredicate = (data: Items, filter: string) => data.name.toLowerCase().includes(filter);
  }

  navigateToAddItem(): void {
    this.router.navigate(['items/add-items']);
  }

  editItem(itemId: string): void {
    this.router.navigate(['items/edit-items', itemId]);
  }

  deleteItem(itemId: string): void {
    this.itemService.deleteItem(itemId).subscribe(
      response => {
        console.log('Item deleted:', response);
        this.dataSource.data = this.dataSource.data.filter(item => item.id !== itemId);
      },
      error => {
        console.error('Error deleting item:', error);
      }
    );
  }

  viewItem(itemId: string): void {
    this.router.navigate(['items/detail-items', itemId]);
  }
}

