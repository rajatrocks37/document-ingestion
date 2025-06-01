package rg.self.documentmanagement.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DocumentResponseDto {
	private Long id;
	private String title;
	private String type;
	private String uploadedBy;
	private LocalDateTime uploadedAt;
	private List<String> keywords;
	private String author;
	private String summary;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "DocumentResponseDto [id=" + id + ", title=" + title + ", type=" + type + ", uploadedBy=" + uploadedBy
				+ ", uploadedAt=" + uploadedAt + ", keywords=" + keywords + ", author=" + author + ", summary="
				+ summary + "]";
	}

}
