package it.aceofwands.memory_bank.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryLink {
    @Embeddable
    @Data
    public static class MemoryLinkKey implements Serializable {
        private UUID sourceDocumentId;
        private UUID destinationDocumentId;
    }

    @EmbeddedId
    private MemoryLinkKey id;

    private MemoryLinkType type;
    private String explanation;
}
