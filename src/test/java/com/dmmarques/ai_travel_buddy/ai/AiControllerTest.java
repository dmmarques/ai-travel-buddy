package com.dmmarques.ai_travel_buddy.ai;

import com.dmmarques.ai_travel_buddy.model.GenActivity;
import com.dmmarques.ai_travel_buddy.model.GenTravelCost;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiController.class)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiService aiService;

    @Test
    void testEndpoint_returnsString() throws Exception {
        Mockito.when(aiService.simpleChat(anyString())).thenReturn("ok");

        mockMvc.perform(get("/travel-buddy/ai/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void test1Endpoint_returnsString() throws Exception {
        Mockito.when(aiService.simpleChat(anyString())).thenReturn("fine");

        mockMvc.perform(get("/travel-buddy/ai/test1"))
                .andExpect(status().isOk())
                .andExpect(content().string("fine"));
    }

    @Test
    void travelSuggestions_returnsListOfActivities() throws Exception {
        List<GenActivity> activities = List.of(
                new GenActivity("Museum", "Main St", LocalDateTime.of(2025, 1, 1, 10, 0), "Sightseeing", new BigDecimal("12.50")),
                new GenActivity("Lunch", "2nd Ave", LocalDateTime.of(2025, 1, 1, 12, 30), "Food", new BigDecimal("20.00"))
        );
        Mockito.when(aiService.chat(anyString())).thenReturn(activities);

        mockMvc.perform(get("/travel-buddy/ai/travelSuggestions")
                        .param("location", "Paris")
                        .param("numberOfDays", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Museum")))
                .andExpect(jsonPath("$[1].category", is("Food")));
    }

    @Test
    void travelCostSuggestions_returnsGenTravelCost() throws Exception {
        GenTravelCost cost = new GenTravelCost(new BigDecimal("30.00"), new BigDecimal("10.00"), new BigDecimal("40.00"));
        Mockito.when(aiService.calculateTravelCost(anyString())).thenReturn(cost);

        mockMvc.perform(get("/travel-buddy/ai/travelCostSuggestions")
                        .param("fromLocation", "A")
                        .param("toLocation", "B")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fuel", is(30.00)))
                .andExpect(jsonPath("$.tollCost", is(10.00)))
                .andExpect(jsonPath("$.totalCost", is(40.00)));
    }
}
