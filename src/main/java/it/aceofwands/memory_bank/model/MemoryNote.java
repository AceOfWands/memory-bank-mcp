package it.aceofwands.memory_bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.aceofwands.memory_bank.util.ClockUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryNote {
    @Id
    private UUID id = UUID.randomUUID();
    private String content;
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 384)
    @JsonIgnore
    private float[] vectorContent;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> keywords;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MemoryLink> links;
    private String context;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;

    private String timestamp = ClockUtils.getCurrentDateTimeString();
    private long lastAccessed = ClockUtils.getCurrentTimestampSeconds();
    private long retrievalCount = 0;
}
