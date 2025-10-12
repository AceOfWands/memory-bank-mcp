package it.aceofwands.memory_bank.service;

import dev.langchain4j.model.embedding.EmbeddingModel;
import it.aceofwands.memory_bank.ai.SemanticAnalyzerAiService;
import it.aceofwands.memory_bank.model.MemoryLink;
import it.aceofwands.memory_bank.model.MemoryNote;
import it.aceofwands.memory_bank.model.retrievability.RetrievabilityFilter;
import it.aceofwands.memory_bank.repository.MemoryNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemoryService {
    private final SemanticAnalyzerAiService semanticAnalyzerAiService;
    private final MemoryNoteRepository repository;
    private final EmbeddingModel embeddingModel;
    @Value("${memory.similarity-threshold:0.75}")
    private final double similarityThreshold;
    @Value("${memory.batch-factor:3}")
    private final int batchFactor;
    private final RetrievabilityFilter retrievabilityFilter;

    @Transactional
    public MemoryNote addMemoryNote(String content) {
        var response = semanticAnalyzerAiService.analyze(content);

        var linkable = this.similaritySearch(content, 5);

        MemoryNote note = new MemoryNote();
        note.setContent(content);
        note.setVectorContent(this.embedText(content));
        note.setKeywords(response.getKeywords());
        note.setContext(response.getContext());
        note.setTags(response.getTags());

        final UUID noteId = note.getId();
        List<MemoryLink> links = linkable.stream()
            .map(linkableNote -> {
                var relation = semanticAnalyzerAiService.generateLinkRelation(content, linkableNote.getContent());
                return Pair.of(linkableNote.getId(), relation);
            })
            .filter(pair -> pair.getSecond().isLinkExists())
            .map(pair -> {
                UUID id = pair.getFirst();
                var details = pair.getSecond();
                MemoryLink.MemoryLinkKey key = new MemoryLink.MemoryLinkKey();
                key.setDestinationDocumentId(id);
                key.setSourceDocumentId(noteId);
                return new MemoryLink(
                    key,
                    details.getType(),
                    details.getExplanation()
                );
            })
            .toList();
        note.setLinks(links);

        return repository.save(note);
    }

    private List<MemoryNote> similaritySearch(String content, int topK) {
        var response = embeddingModel.embed(content);
        var vector = response.content().vector();
        return repository.similaritySearch(
            vector,
            similarityThreshold,
            topK
        );
    }

    @Transactional
    public List<MemoryNote> searchNotes(String context) {
        List<MemoryNote> similaritySelected = similaritySearch(context, 5 * batchFactor);
        var ids = similaritySelected.stream().map(MemoryNote::getId).toList();
        if (!ids.isEmpty()) repository.incrementRetrievalCountAndLastAccessByIds(ids);

        return similaritySelected
            .stream()
            .filter(note -> {
                long lastAccessedMillis = note.getLastAccessed();
                long lastAccessed = TimeUnit.MILLISECONDS.toSeconds(lastAccessedMillis);
                long retrievalCount = note.getRetrievalCount();
                return retrievabilityFilter.accept(retrievalCount, lastAccessed);
            })
            .limit(5)
            .toList();
    }

    @Transactional
    public List<MemoryNote> getNotes(List<UUID> ids) {
        List<MemoryNote> notes = Collections.emptyList();
        if (!ids.isEmpty()) {
            notes = repository.findAllById(ids);
            repository.incrementRetrievalCountAndLastAccessByIds(ids);
        }
        return notes;
    }

    private float[] embedText(String text) {
        var response = embeddingModel.embed(text);
        var content = response.content();
        return content.vector();
    }
}
