package com.zv.hash.zvchain;

import lombok.Data;

@Data
public class Response<T> {
    private T result;
    private Error error;
}
