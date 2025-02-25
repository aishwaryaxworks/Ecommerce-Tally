package com.netzwerk.ecommerce.dto;

import lombok.Data;

@Data
public class ResponseDTO {

    private boolean error;
    private String msg;
    private DataDTO data;

}
