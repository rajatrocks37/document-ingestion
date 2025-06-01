package rg.self.documentmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rg.self.documentmanagement.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

	@Query(value = "SELECT * FROM document d WHERE to_tsvector('english', d.content) @@ plainto_tsquery('english', :keyword)", nativeQuery = true)
	List<Document> searchByKeyword(@Param("keyword") String keyword);

	@Query("SELECT d FROM Document d WHERE "
			+ "(:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
			+ "(:type IS NULL OR LOWER(d.type) LIKE LOWER(CONCAT('%', :type, '%'))) AND "
			+ "(:uploadedBy IS NULL OR LOWER(d.uploadedBy) LIKE LOWER(CONCAT('%', :uploadedBy, '%')))")
	Page<Document> findByFilters(@Param("title") String title, @Param("type") String type,
			@Param("uploadedBy") String uploadedBy, Pageable pageable);

}