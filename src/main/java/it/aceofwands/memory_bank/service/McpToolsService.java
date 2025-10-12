package it.aceofwands.memory_bank.service;

import it.aceofwands.memory_bank.model.MemoryNote;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class McpToolsService {
    private final MemoryService memoryService;

    @Tool(
        description = "Get memory notes by their IDs",
        name = "get_memory_notes_by_ids"
    )
    public List<MemoryNote> getMemoryNotesByIds(List<UUID> ids) {
        return memoryService.getNotes(ids);
    }

    @Tool(
        description = "Search for relevant memory notes based on a context",
        name = "search_memory_notes"
    )
    public List<MemoryNote> searchMemoryNotes(String context) {
        return memoryService.searchNotes(context);
    }

    @Tool(
        description = "Add a new memory note with the given content",
        name = "add_memory_note"
    )
    public MemoryNote addMemoryNote(String content) {
        return memoryService.addMemoryNote(content);
    }
}
