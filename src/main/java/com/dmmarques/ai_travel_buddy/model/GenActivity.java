package com.dmmarques.ai_travel_buddy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenActivity {
    private String name;
    private String address;
    private LocalDateTime date;
    private String category;
    private BigDecimal cost;
}