package it.aceofwands.memory_bank.repository;

import it.aceofwands.memory_bank.model.MemoryNote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface MemoryNoteRepository extends ListCrudRepository<MemoryNote, UUID> {
    @Modifying
    @Query("UPDATE MemoryNote m SET m.retrievalCount = m.retrievalCount + 1, m.lastAccessed = EXTRACT(EPOCH FROM CURRENT_TIMESTAMP()) WHERE m.id IN :ids")
    void incrementRetrievalCountAndLastAccessByIds(Iterable<UUID> ids);

    @NativeQuery("SELECT * FROM memory_note m ORDER BY vector_content <=> cast(:vector as vector) >= :threshold LIMIT :topK")
    List<MemoryNote> similaritySearch(float[] vector, double threshold, int topK);
}
