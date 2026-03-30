package com.power.exceptions;

import com.power.result.ResponseStatusEnum;

public  class GraceException {
    public  static void dispaly(ResponseStatusEnum statusEnum) {
        throw new MyCustomException(statusEnum);
    }
}
