package it.aceofwands.memory_bank.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import it.aceofwands.memory_bank.model.MemoryLinkType;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@AiService
public interface SemanticAnalyzerAiService {
    @Data
    class AnalysisResponse {
        private List<String> keywords;
        private String context;
        private List<String> tags;
    }

    @SystemMessage("""
        Generate a structured analysis of the following content by:
        1. Identifying the most salient keywords (focus on nouns, verbs, and key concepts)
        2. Extracting core themes and contextual elements
        3. Creating relevant categorical tags

        Format the response as a JSON object:
        {
            "keywords": [
                // several specific, distinct keywords that capture key concepts and terminology
                // Order from most to least important
                // Don't include keywords that are the name of the speaker or time
                // At least three keywords, but don't be too redundant.
            ],
            "context":\s
                // one sentence summarizing:
                // - Main topic/domain
                // - Key arguments/points
                // - Intended audience/purpose
            ,
            "tags": [
                // several broad categories/themes for classification
                // Include domain, format, and type tags
                // At least three tags, but don't be too redundant.
            ]
        }
    
        The following user message will provide the content to analyze.
    """)
    AnalysisResponse analyze(@UserMessage String content);

    @Data
    class LinkRelationResponse {
        private boolean linkExists;
        @Nullable
        private MemoryLinkType type;
        @Nullable
        private String explanation;
    }

    @SystemMessage("""
        You are a “Zettelkasten Link Reasoner.”
        Given two notes, infer whether they should be linked and explain *why*.
    
        Each note expresses one autonomous memory note. Your goal is to find **conceptual relationships** — not word similarity.
    
        * Identify if a meaningful link exists.
        * Classify the relation (causal, comparative, example, contrast, generalization, consequence, etc.).
        * Write a short explanation (1–2 sentences) describing why they relate.
    
        Think like a researcher in systems theory: focus on *how ideas influence, clarify, or extend one another*, not on surface similarity.
    """)
    @UserMessage("""
        Compare the following two notes and determine the conceptual or logical reason to link the destination node to the source note.
    
        Source note:
        {noteS}
    
        destination note:
        {noteD}
    """)
    LinkRelationResponse generateLinkRelation(
        @V("noteS") String linkSourceContent,
        @V("noteD") String linkDestinationContent
    );
}
