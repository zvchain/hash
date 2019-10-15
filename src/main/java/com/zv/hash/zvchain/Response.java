package com.zv.hash.zvchain;

import lombok.Data;
import com.zv.hash.zvchain.model.Error;

@Data
public class Response<T> {
    private T result;
    private Error error;
}
