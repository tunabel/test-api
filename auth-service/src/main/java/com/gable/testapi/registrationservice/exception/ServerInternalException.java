package com.gable.testapi.registrationservice.exception;

public class ServerInternalException extends RuntimeException{

  public ServerInternalException() {
    super();
  }

  public ServerInternalException(String message) {
    super(message);
  }

  public ServerInternalException(String message, Throwable cause) {
    super(message, cause);
  }
}
