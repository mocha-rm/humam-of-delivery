package com.teamnine.humanofdelivery.exception.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderException extends RuntimeException {
  private final OrderErrorCode orderErrorCode;

  @Override
  public String getMessage() {
    return orderErrorCode.getMessage();
  }
}
