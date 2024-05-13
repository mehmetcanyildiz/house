package com.apartment.house.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggerService {

  public void logInfo(String message) {
    log.info(message);
  }

  public void logError(String message, Throwable throwable) {
    log.error(message, throwable);
  }

  public void logWarn(String message) {
    log.warn(message);
  }

  public void logDebug(String message) {
    log.debug(message);
  }
}
