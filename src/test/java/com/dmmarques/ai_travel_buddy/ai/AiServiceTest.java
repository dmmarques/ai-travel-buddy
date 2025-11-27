package com.dmmarques.ai_travel_buddy.ai;

import com.dmmarques.ai_travel_buddy.model.GenActivity;
import com.dmmarques.ai_travel_buddy.model.GenTravelCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AiServiceTest {

    private ChatClient.Builder builder;
    private ChatClient chatClient;
    private AiService aiService;

    @BeforeEach
    void setUp() {
        // Deep stubs allow us to mock chained calls like prompt().call().content()/entity()
        chatClient = mock(ChatClient.class, Answers.RETURNS_DEEP_STUBS);
        builder = mock(ChatClient.Builder.class);
        when(builder.build()).thenReturn(chatClient);
        aiService = new AiService(builder);
    }

    @Test
    void simpleChat_returnsContentFromChatClient() {
        when(chatClient.prompt(anyString()).call().content()).thenReturn("hello world");

        String result = aiService.simpleChat("test prompt");

        assertThat(result).isEqualTo("hello world");
    }

    @Test
    void chat_returnsListOfGenActivity() {
        //GIVEN
        List<GenActivity> activities = List.of(
                new GenActivity("Museum", "Main St", LocalDateTime.of(2025, 1, 1, 10, 0),
                                "Sightseeing", new BigDecimal("12.50")),
                new GenActivity("Lunch", "2nd Ave", LocalDateTime.of(2025, 1, 1, 12, 30),
                                "Food", new BigDecimal("20.00"))
        );

        //WHEN
        when(chatClient
                .prompt(anyString())
                .call()
                .entity(any(ParameterizedTypeReference.class)))
                .thenReturn(activities);

        //THEN
        List<GenActivity> result = aiService.chat("plan");

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("Museum");
        assertThat(result.get(1).getCategory()).isEqualTo("Food");
    }

    @Test
    void calculateTravelCost_returnsGenTravelCost() {
        GenTravelCost cost = new GenTravelCost(new BigDecimal("30.00"), new BigDecimal("10.00"), new BigDecimal("40.00"));

        when(chatClient
                .prompt(anyString())
                .call()
                .entity(any(ParameterizedTypeReference.class)))
                .thenReturn(cost);

        GenTravelCost result = aiService.calculateTravelCost("from-to");

        assertThat(result.getFuel()).isEqualByComparingTo("30.00");
        assertThat(result.getTollCost()).isEqualByComparingTo("10.00");
        assertThat(result.getTotalCost()).isEqualByComparingTo("40.00");
    }
}
