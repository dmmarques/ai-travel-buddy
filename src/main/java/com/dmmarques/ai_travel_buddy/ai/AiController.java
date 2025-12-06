package com.dmmarques.ai_travel_buddy.ai;

import com.dmmarques.ai_travel_buddy.model.GenActivity;
import com.dmmarques.ai_travel_buddy.model.GenTravelCost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/travel-buddy/ai")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final AiService aiService;

    @GetMapping("/test")
    public String test() {
        return aiService.simpleChat("Create an itinerary for 3 days in Castelo Branco, can you give me the coordinates, address, and the string for me to look it up in google "
                                        + "places "
                                        + "API for those places as well ? Keep "
                                        + "responses "
                                        + "simple and short"
                                        + ".");
    }

    @GetMapping("/test1")
    public String test1() {
        return aiService.simpleChat("What are the best options for staying in Castelo Branco for 3 days ? Give me the Booking ranking.");
    }

    @GetMapping("/travelSuggestions")
    public List<GenActivity> generateTravelSuggestions(@RequestParam("arrivalDate") String arrivalDate, @RequestParam("location") String location,
        @RequestParam("numberOfDays") String nrDays,
        @RequestParam("preferences") List<String> preferences) {
        log.info("Generating travel suggestions for {} days in {} with the following preferences: {}. Starting on {}", nrDays, location, preferences, arrivalDate);
        return aiService.chat(String.format("Create an itinerary for each day while I am visiting %s."
                                                + "I am arriving on %s and I am staying for %s days."
                                                + "I have special preference for activities related to: %s"
                                                + "Keep responses simple and short.", location, arrivalDate, nrDays, preferences));
    }

    @GetMapping("/travelCostSuggestions")
    public GenTravelCost calculateTravelCost(@RequestParam("fromLocation") String from, @RequestParam("toLocation") String to) {
        log.info("Calculating travel cost from {} to {}", from, to);
        return aiService.calculateTravelCost(String.format("What is the cost of travelling by Car from %s to %s ? Keep the response simple and short.", from, to));
    }

}
