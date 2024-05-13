package com.apartment.house.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

  private boolean status = false;
  private Integer errorCode;
  private String error;
  private String businessErrorDescription;
  private Set<String> validationErrors;
  private Map<String, String> errors;
}
