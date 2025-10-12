package it.aceofwands.memory_bank.service;

import it.aceofwands.memory_bank.model.MemoryNote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class McpToolsServiceTest {

    @Mock
    MemoryService memoryService;

    @Test
    void testGetMemoryNotesByIds() {
        McpToolsService mcpToolsService = new McpToolsService(memoryService);
        List<UUID> ids = List.of(UUID.randomUUID());
        MemoryNote note = new MemoryNote();
        note.setContent("Note 1");
        List<MemoryNote> mockNotes = List.of(note);

        when(memoryService.getNotes(ids)).thenReturn(mockNotes);

        List<MemoryNote> result = mcpToolsService.getMemoryNotesByIds(ids);

        assertEquals(mockNotes, result);
        verify(memoryService, times(1)).getNotes(ids);
    }

    @Test
    void testSearchMemoryNotes() {
        McpToolsService mcpToolsService = new McpToolsService(memoryService);
        String context = "search term";
        MemoryNote note = new MemoryNote();
        note.setContent("Result Note");
        List<MemoryNote> mockNotes = List.of(note);

        when(memoryService.searchNotes(context)).thenReturn(mockNotes);

        List<MemoryNote> result = mcpToolsService.searchMemoryNotes(context);

        assertEquals(mockNotes, result);
        verify(memoryService, times(1)).searchNotes(context);
    }

    @Test
    void testAddMemoryNote() {
        McpToolsService mcpToolsService = new McpToolsService(memoryService);
        String content = "new note";
        MemoryNote mockNote = new MemoryNote();
        mockNote.setContent(content);

        when(memoryService.addMemoryNote(content)).thenReturn(mockNote);

        MemoryNote result = mcpToolsService.addMemoryNote(content);

        assertEquals(mockNote, result);
        verify(memoryService, times(1)).addMemoryNote(content);
    }
}