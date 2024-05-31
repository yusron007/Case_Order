import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:8080/api/order';
  private apiUrl2 = 'http://localhost:8080/api/customerName';
  private apiUrl3 = 'http://localhost:8080/api/items/getName';
  private apiReport = 'http://localhost:8080/api/order/report';

  constructor(private http: HttpClient) { }

  getOrder(page: number, size: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/get`, { params });
  }

  getOrderById(orderId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getById/${orderId}`);
  }

  createOrder(orderData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, orderData);
  }

  updateOrder(orderId: string, orderData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update/${orderId}`, orderData);
  }

  deleteOrder(orderId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/delete/${orderId}`);
  }

  getNameCustomer(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl2}`);
  }

  getNameItems(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl3}`);
  }

  getReport(): Observable<any> {
    return this.http.get(this.apiReport, { responseType: 'blob' });
  }
}
