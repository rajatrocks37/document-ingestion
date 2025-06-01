import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../shared/auth.service';

type FilterKeys = 'title' | 'type' | 'uploadedBy' | 'uploadedAt' | 'keywords' | 'startDate';

interface Document {
  title: string;
  type: string;
  uploadedBy: string;
  uploadedAt: string;
  keywords: string[];
  id: number;
}

interface PagedResponse {
  content: Document[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

@Component({
  selector: 'app-manage',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage.component.html',
  styleUrl: './manage.component.css'
})
export class ManageComponent {
  documents: Document[] = [];
  filters: Record<FilterKeys, string> = {
    title: '',
    type: '',
    uploadedBy: '',
    uploadedAt: '',
    keywords: '',
    startDate: ''
  };

  page = 0;
  size = 2;
  totalPages = 0;
  currentPage = 0;
  sortField = 'uploadedAt';
  sortDir = 'desc';

  constructor(private http: HttpClient, private auth: AuthService) {
    this.fetchDocs();
  }

  fetchDocs() {
    let params = new HttpParams()
      .set('page', this.currentPage.toString())
      .set('size', this.size)
      .set('sort', `${this.sortField},${this.sortDir}`);

    Object.entries(this.filters).forEach(([key, value]) => {
      if (value) params = params.set(key, value);
    });

    this.http.get<PagedResponse>('http://localhost:8080/api/documents', { params }).subscribe(res => {
      this.documents = res.content;
      this.totalPages = res.totalPages;
    });
  }

  deleteDoc(id: number) {
    this.http.delete(`http://localhost:8080/api/documents/${id}`)
      .subscribe(() => this.fetchDocs());
  }

  changePage(delta: number) {
    this.page += delta;
    this.fetchDocs();
  }

  sortBy(field: string) {
    if (this.sortField === field) {
      this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDir = 'asc';
    }
    this.fetchDocs();
  }

  applyFilters() {
    this.currentPage = 0;
    this.fetchDocs();
  }

  resetFilters() {
    this.filters = {
      title: '',
      type: '',
      uploadedBy: '',
      uploadedAt: '',
      keywords: '',
      startDate: ''
    };
    // this.sortBy = '';
    this.sortDir = 'asc';
    this.currentPage = 0;
    this.fetchDocs();
  }

   goToPage(page: number) {
    this.currentPage = page;
    this.fetchDocs();
  }
}
