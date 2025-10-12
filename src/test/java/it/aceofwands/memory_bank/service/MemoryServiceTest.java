package it.aceofwands.memory_bank.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import it.aceofwands.memory_bank.ai.SemanticAnalyzerAiService;
import it.aceofwands.memory_bank.model.MemoryLinkType;
import it.aceofwands.memory_bank.model.MemoryNote;
import it.aceofwands.memory_bank.model.retrievability.RetrievabilityFilter;
import it.aceofwands.memory_bank.repository.MemoryNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemoryServiceTest {
    @Mock
    private SemanticAnalyzerAiService semanticAnalyzerAiService;
    @Mock
    private MemoryNoteRepository repository;
    @Mock
    private EmbeddingModel embeddingModel;
    @Mock
    private RetrievabilityFilter retrievabilityFilter;

    private MemoryService memoryService;

    @BeforeEach
    void beforeEach() {
        memoryService = new MemoryService(
                semanticAnalyzerAiService,
                repository,
                embeddingModel,
                0.4,
                2,
                retrievabilityFilter
        );
    }

    @Test
    void addMemoryNoteSavesNoteWithLinks() {
        String content = "Artificial intelligence and memory systems";
        float[] vector = new float[]{0.1f, 0.2f, 0.3f};
        var embeddingContent = new Embedding(vector);
        var embeddingResponse = new Response<>(embeddingContent);
        when(embeddingModel.embed(content)).thenReturn(embeddingResponse);

        var analysisResponse = new SemanticAnalyzerAiService.AnalysisResponse();
        analysisResponse.setContext("test context");
        analysisResponse.setTags(List.of("ai", "computer science", "technology"));
        analysisResponse.setKeywords(List.of("technology"));
        when(semanticAnalyzerAiService.analyze(content)).thenReturn(analysisResponse);

        MemoryNote existingNote = new MemoryNote();
        existingNote.setId(UUID.randomUUID());
        existingNote.setContent("Existing note content");

        when(repository.similaritySearch(any(), anyDouble(), anyInt()))
                .thenReturn(List.of(existingNote));

        var linkRelation = new SemanticAnalyzerAiService.LinkRelationResponse();
        linkRelation.setLinkExists(true);
        linkRelation.setType(MemoryLinkType.DERIVED_CONCEPT);
        linkRelation.setExplanation("AI and memory are connected");
        when(semanticAnalyzerAiService.generateLinkRelation(anyString(), anyString()))
                .thenReturn(linkRelation);

        when(repository.save(any(MemoryNote.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MemoryNote savedNote = memoryService.addMemoryNote(content);

        assertNotNull(savedNote);
        assertEquals(content, savedNote.getContent());
        assertArrayEquals(vector, savedNote.getVectorContent());
        assertEquals(List.of("technology"), savedNote.getKeywords());
        assertEquals("test context", savedNote.getContext());
        assertEquals(1, savedNote.getLinks().size());

        var link = savedNote.getLinks().get(0);
        assertEquals(linkRelation.getType(), link.getType());
        assertEquals(linkRelation.getExplanation(), link.getExplanation());

        verify(repository, times(1)).save(any(MemoryNote.class));
        verify(semanticAnalyzerAiService, times(1)).analyze(content);
    }

    @Test
    void searchNotesReturnsFilteredResults() {
        String context = "memory";
        float[] vector = new float[]{0.5f, 0.6f};
        var embeddingContent = new Embedding(vector);
        var embeddingResponse = new Response<>(embeddingContent);
        when(embeddingModel.embed(context)).thenReturn(embeddingResponse);

        MemoryNote n1 = new MemoryNote();
        n1.setId(UUID.randomUUID());
        n1.setRetrievalCount(5);
        n1.setLastAccessed(System.currentTimeMillis());

        MemoryNote n2 = new MemoryNote();
        n2.setId(UUID.randomUUID());
        n2.setRetrievalCount(1);
        n2.setLastAccessed(System.currentTimeMillis() - 99999);

        when(repository.similaritySearch(any(), anyDouble(), anyInt()))
                .thenReturn(List.of(n1, n2));

        when(retrievabilityFilter.accept(anyLong(), anyLong()))
                .thenReturn(true);

        List<MemoryNote> result = memoryService.searchNotes(context);

        assertEquals(2, result.size());
        verify(repository, times(1)).incrementRetrievalCountAndLastAccessByIds(anyList());
        verify(retrievabilityFilter, times(2)).accept(anyLong(), anyLong());
    }

    @Test
    void getNotesReturnsNotesAndIncrementCounters() {
        UUID id = UUID.randomUUID();
        MemoryNote note = new MemoryNote();
        note.setId(id);

        when(repository.findAllById(List.of(id))).thenReturn(List.of(note));

        List<MemoryNote> result = memoryService.getNotes(List.of(id));

        assertEquals(1, result.size());
        assertEquals(note, result.get(0));
        verify(repository, times(1)).incrementRetrievalCountAndLastAccessByIds(List.of(id));
    }

    @Test
    void getNotesReturnsEmptyListWhenIdsEmpty() {
        List<MemoryNote> result = memoryService.getNotes(List.of());

        assertTrue(result.isEmpty());
        verify(repository, never()).findAllById(any());
    }

}