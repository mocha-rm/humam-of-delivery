package com.teamnine.humanofdelivery.exception.store;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreException extends RuntimeException {
    private final StoreErrorCode storeErrorCode;

    @Override
    public String getMessage() {
        return storeErrorCode.getMessage();
    }
}
