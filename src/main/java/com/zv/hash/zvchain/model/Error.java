package com.zv.hash.zvchain.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lt
 * @Date 6:40 下午 2019/10/15
 */
@Data
@ToString
public class Error {
    private int code;
    private String message;
}
