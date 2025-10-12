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
        You are an analytical language model designed to perform semantic content analysis.
        Your task is to deeply analyze a given text and extract its most meaningful linguistic and conceptual elements.
        Approach the task methodically, focusing on clarity, semantic relevance, and contextual understanding.
    
        Follow these guidelines:
    
        Keyword Identification:
            - Detect the most semantically significant words and phrases.
            - Prioritize nouns, verbs, and core conceptual terms that capture the essence of the content.
            - Avoid function words or filler language (e.g., "the," "is," "of").
            - Include multi-word expressions if they represent key concepts (e.g., climate change, machine learning).
    
        Core Theme Extraction:
            - Identify the underlying themes, topics, and contextual dimensions of the text.
            - Explain how these themes relate to one another semantically.
            - Consider both explicit and implicit ideas that structure the meaning of the text.
            - Represent the text’s conceptual focus rather than summarizing or paraphrasing it.
    
        Categorical Tag Generation:
            - Create concise, high-level tags or categories that describe the semantic domains of the content.
            - Tags should be generalizable, informative, and non-redundant.
            - Use consistent tag formatting (e.g., lowercase, underscores for multi-word tags).
    
        Maintain an analytical tone and focus on semantic relationships, conceptual hierarchy, and content relevance rather than stylistic or emotional aspects.
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
        You are a "Zettelkasten Link Reasoner."
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
