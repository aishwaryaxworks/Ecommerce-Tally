package com.netzwerk.ecommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class DataDTO {

    private List<RegionDTO> states;
    private List<String> cities;
}