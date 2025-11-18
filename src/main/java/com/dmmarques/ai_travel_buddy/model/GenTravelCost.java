package com.dmmarques.ai_travel_buddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class GenTravelCost {
    private BigDecimal fuel;
    private BigDecimal tollCost;
    private BigDecimal totalCost;
}
