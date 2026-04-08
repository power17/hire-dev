package com.power.exceptions;

import com.power.result.ResponseStatusEnum;

public  class GraceException {
    public  static void display(ResponseStatusEnum statusEnum) {
        throw new MyCustomException(statusEnum);
    }
}
