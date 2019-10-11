package com.tas.hash.zvchain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ZvRequest {
    private String jsonrpc = "2.0";
    private String method;
    private String id = "1234";
    private List<Object> params = new ArrayList<>();
}
