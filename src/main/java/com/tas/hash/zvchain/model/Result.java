package com.tas.hash.zvchain.model;

import lombok.Data;

/**
 * @author kaede
 * @create 2019-08-13 10:21
 **/
@Data
public class Result<T> {
    private String message;
    private Integer status;
    private T data;
}
