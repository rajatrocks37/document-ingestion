import { CommonModule } from "@angular/common";
import { HttpClient, HttpEventType } from "@angular/common/http";
import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AuthService } from "../../shared/auth.service";

@Component({
  standalone: true,
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  imports: [CommonModule, FormsModule],
  styleUrl: './upload.component.css'
})
export class UploadComponent {

  file: File | null = null;
  uploading = false;
  uploadSuccess = false;
  uploadError = false;
  uploadProgress = 0;

  constructor(private http: HttpClient, private auth: AuthService) { }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.file = input.files[0];
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
  }

  onFileDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      this.file = event.dataTransfer.files[0];
    }
  }

  upload() {
    if (!this.file) {
      this.uploadError = true;
      return;
    }

    const formData = new FormData();
    formData.append('file', this.file);

    this.uploading = true;
    this.uploadSuccess = false;
    this.uploadError = false;
    this.uploadProgress = 0;


    this.http.post('http://localhost:8080/api/documents/upload', formData, {
      reportProgress: true,
      observe: 'events'
    }).subscribe({
      next: (event) => {
        if (event.type === HttpEventType.UploadProgress && event.total) {
          console.log("event >> ", event);
          this.uploadProgress = Math.round((event.loaded / event.total) * 100);
          console.log("this.uploadProgress >> ",this.uploadProgress);
        } else if (event.type === HttpEventType.Response) {
          console.log("else if HttpEventType.Response case event >> ", event);
          this.uploading = false;
          this.uploadSuccess = true;
          this.uploadProgress = 0;
          this.resetForm();
        }else{
          console.log("else case event >> ", event);
        }
      },
      error: () => {
        this.uploading = false;
        this.uploadError = true;
      }
    });
  }

  private resetForm() {
    this.file = null;
  }
}
