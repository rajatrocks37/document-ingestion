import { CommonModule, formatDate } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Component, signal } from "@angular/core";
import { AuthService } from "../shared/auth.service";
import { FormsModule } from "@angular/forms";

@Component({
  standalone: true,
  selector: 'app-ingestion',
  templateUrl: './ingestion.component.html',
  styleUrls: ['./ingestion.component.css'],
  imports: [CommonModule, FormsModule]
})
export class IngestionComponent {
  statusList: any[] = [];
  docId: number | null = null;
  ingestionStatus = signal<
    { docId: number; status: string; timestamp: string }[]
  >([]);

  constructor(private http: HttpClient, private auth: AuthService) {
    // this.getStatuses();
  }

  // triggerIngestion() {
  //   this.http.post('http://localhost:8080/api/ingestion/start', {}, {
  //     headers: { Authorization: `Bearer ${this.auth.token()}` }
  //   }).subscribe(() => this.getStatuses());
  // }

  // getStatuses() {
  //   this.http.get<any[]>('http://localhost:8080/api/ingestion/status', {
  //     headers: { Authorization: `Bearer ${this.auth.token()}` }
  //   }).subscribe(data => this.statusList = data);
  // }

  triggerIngestion() {
    if (!this.docId) {
      alert('Please enter a valid Document ID.');
      return;
    }

    const timestamp = formatDate(new Date(), 'short', 'en-US');
    this.ingestionStatus.update((prev: any) => [
      ...prev,
      { docId: this.docId!, status: 'IN_PROGRESS', timestamp }
    ]);

    this.http
      .post(`http://localhost:8080/api/ingest/${this.docId}`, {}, {
        headers: { Authorization: `Bearer ${this.auth.token()}` }
      })
      .subscribe({
        next: () => {
          this.updateStatus('COMPLETED');
        },
        error: () => {
          this.updateStatus('FAILED');
        }
      });
  }

  private updateStatus(finalStatus: 'COMPLETED' | 'FAILED') {
    const index = this.ingestionStatus().findIndex((item: { docId: number | null; }) => item.docId === this.docId);
    if (index !== -1) {
      const updated = [...this.ingestionStatus()];
      updated[index] = {
        ...updated[index],
        status: finalStatus,
        timestamp: formatDate(new Date(), 'short', 'en-US')
      };
      this.ingestionStatus.set(updated);
    }
  }
}
