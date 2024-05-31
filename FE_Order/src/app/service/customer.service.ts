import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getCustomers(page: number, size: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/getCustomer`,  { params });
  }

  getCustomerById(customerId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getCustomer/${customerId}`);
  }

  createCustomer(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, formData);
  }

  updateCustomer(customerId: string, formData: FormData): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update/${customerId}`, formData);
  }

  deleteCustomer(customerId: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/delete/${customerId}`);
  }
}
