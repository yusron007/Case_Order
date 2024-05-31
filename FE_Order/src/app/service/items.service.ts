import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ItemsService {

  private apiUrl = 'http://localhost:8080/api/items';

  constructor(private http: HttpClient) { }

  getItems(page: number, size: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/get`, { params });
  }

  getItemById(itemsId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getById/${itemsId}`);
  }

  createItem(itemData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, itemData);
  }

  updateItem(itemsId: string, itemData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update/${itemsId}`, itemData);
  }

  deleteItem(itemsId: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/delete/${itemsId}`);
  }
}
