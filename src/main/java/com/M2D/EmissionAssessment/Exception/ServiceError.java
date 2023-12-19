package com.M2D.EmissionAssessment.Exception;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

import static org.springframework.http.HttpStatus.*;

public enum ServiceError
{
  G0000("General processing exception", INTERNAL_SERVER_ERROR, ResponseType.ERROR),
  // Unimplemented in application
  G0100("Temporarily Unavailable or not implemented", SERVICE_UNAVAILABLE, ResponseType.ERROR),
  // reserved for Timeout to external services. 503
  G0101("Integration timeout or not accessible", GATEWAY_TIMEOUT, ResponseType.ERROR),
  // Reserved for 400 General Request Validation
  G0400("Request validation error.", BAD_REQUEST, ResponseType.ERROR),
  // Reserved for general not found 404
  G0401("Not found", NOT_FOUND, ResponseType.ERROR),
  // Reserved for method not supported
  G0402("Request method not supported", METHOD_NOT_ALLOWED, ResponseType.ERROR),
  // reserved for 415 invalid media type
  G0403("Invalid media type", UNSUPPORTED_MEDIA_TYPE, ResponseType.ERROR),
  // added double single quotes, this will be treated as single quot in
  // MessageFormat.format method to replace the {0}
  GO503("We couldn''t connect to {0}. Please try again.", SERVICE_UNAVAILABLE, ResponseType.ERROR),
  G0404("Record not found.", NOT_FOUND, ResponseType.ERROR),

  US001("User not found.", NOT_FOUND, ResponseType.ERROR),
  EM001("Token not Found.", NOT_FOUND, ResponseType.ERROR),
  EM002("Email not found.", NOT_FOUND, ResponseType.ERROR),
  EA001("TCO2 not found.", NOT_FOUND, ResponseType.ERROR),
  EA002("Baseline not found.", NOT_FOUND, ResponseType.ERROR),


  ;

  private String message;
  private HttpStatus httpStatus;
  private String responseType;

  ServiceError(String message, HttpStatus httpStatus, String responseType)
  {
    this.message = message;
    this.httpStatus = httpStatus;
    this.responseType = responseType;
  }

  public String getMessage()
  {
    return message;
  }

  public HttpStatus getHttpStatus()
  {
    return this.httpStatus;
  }

  public String getResponseType()
  {
    return responseType;
  }

  @Override
  public String toString()
  {
    return getMessage();
  }

  /**
   * Format the message string system token if any by replacing with the system name e.g Jira or Confluence
   *
   * @param systemName
   * @return formated string
   */
  public String getMessageForTargetSystem(String systemName)
  {
    return MessageFormat.format(message, systemName);
  }


  private static class ResponseType
  {
    private static final String ERROR = "error";
    private static final String WARNING = "warning";
  }
}