import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AuthService } from '../shared/auth.service';

@Component({
  standalone: true,
  selector: 'app-ask',
  imports: [CommonModule, FormsModule],
  templateUrl: './ask.component.html',
  styleUrl: './ask.component.css'
})
export class AskComponent {
  question = '';
  results: { title: string; snippet: string }[] = [];
  loading = false;

  constructor(private http: HttpClient) { }

  ask() {
    if (!this.question.trim()) return;

    this.loading = true;
    this.results = [];

    const params = new HttpParams().set('question', this.question);
    this.http.get<any[]>('http://localhost:8080/api/qna', { params }).subscribe({
      next: res => {
        this.results = res.map(item => ({
          ...item,
          snippet: this.highlightMatch(item.snippet, this.question)
        }));
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  highlightMatch(text: string, keyword: string): string {
    const regex = new RegExp(`(${keyword})`, 'gi');
    return text.replace(regex, `<mark class="bg-yellow-200 font-semibold">$1</mark>`);
  }
}
