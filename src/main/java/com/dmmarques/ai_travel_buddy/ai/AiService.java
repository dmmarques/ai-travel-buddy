package com.dmmarques.ai_travel_buddy.ai;

import com.dmmarques.ai_travel_buddy.model.GenActivity;
import com.dmmarques.ai_travel_buddy.model.GenTravelCost;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {

    private ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public String simpleChat(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }

    public List<GenActivity> chat(String prompt) {
        return chatClient.prompt(prompt).call().entity(new ParameterizedTypeReference<List<GenActivity>>() {});
    }

    public GenTravelCost calculateTravelCost(String prompt) {
        return chatClient.prompt(prompt).call().entity(new ParameterizedTypeReference<GenTravelCost>() {});
    }

}
