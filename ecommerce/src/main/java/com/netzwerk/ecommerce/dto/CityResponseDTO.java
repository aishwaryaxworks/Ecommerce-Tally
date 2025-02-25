package com.netzwerk.ecommerce.dto;

import lombok.Data;

import java.util.List;


@Data
public class CityResponseDTO {
    private boolean error;
    private String msg;
    private List<String> data;
}
