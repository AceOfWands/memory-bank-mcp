package it.aceofwands.memory_bank.configuration;

import it.aceofwands.memory_bank.service.McpToolsService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolsServerConfiguration {
    @Bean
    public ToolCallbackProvider weatherTools(McpToolsService mcpToolsService) {
        return MethodToolCallbackProvider.builder().toolObjects(mcpToolsService).build();
    }
}
