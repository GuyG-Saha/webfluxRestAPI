package com.reactive.webflux2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessStatusUpdate {
    private String status;
}
