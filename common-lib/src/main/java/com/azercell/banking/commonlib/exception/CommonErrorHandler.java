package com.azercell.banking.commonlib.exception;

import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;
import com.azercell.banking.commonlib.exception.model.CommonErrorResponse;
import com.azercell.banking.commonlib.util.ErrorUtil;
import com.azercell.banking.commonlib.util.MessageSourceUtil;
import com.azercell.banking.commonlib.util.WebUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.v;
import static org.springframework.http.HttpStatus.*;

@Log4j2
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "common.error-handler", name = "enabled")
public class CommonErrorHandler extends ResponseEntityExceptionHandler {

    @Autowired
    protected WebUtil webUtil;

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    public CommonErrorResponse handleInvalidInputException(InvalidInputException ex) {
        addErrorLog(BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), "InvalidInputException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(AlreadyExistException.class)
    public CommonErrorResponse handleAlreadyExistException(AlreadyExistException ex) {
        addErrorLog(BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), "AlreadyExistException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public CommonErrorResponse handleDataNotFoundException(DataNotFoundException ex) {
        addErrorLog(NOT_FOUND, ex.getErrorCode(), ex.getMessage(), "DataNotFoundException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceMissingException.class)
    public CommonErrorResponse handleResourceMissingException(ResourceMissingException ex) {
        addErrorLog(NOT_FOUND, ex.getErrorCode(), ex.getMessage(), "ResourceMissingException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public CommonErrorResponse handleForbiddenException(ForbiddenException ex) {
        addErrorLog(FORBIDDEN, ex.getErrorCode(), ex.getMessage(), "ForbiddenException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        addErrorLog(BAD_REQUEST, CommonErrorCode.PARAMETER_INVALID, ex.getMessage(), "MethodArgumentTypeMismatchException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.PARAMETER_INVALID,
                ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public CommonErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        addErrorLog(BAD_REQUEST, CommonErrorCode.PARAMETER_INVALID, ex.getMessage(), "MissingRequestHeaderException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.PARAMETER_INVALID,
                ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public CommonErrorResponse handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        addErrorLog(BAD_REQUEST, CommonErrorCode.PARAMETER_INVALID, ex.getMessage(), "MaxUploadSizeExceededException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.PARAMETER_INVALID,
                ex.getMostSpecificCause().getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        final Optional<ConstraintViolation<?>> violation = ex.getConstraintViolations().stream().findAny();
        String errorMessage;
        String errorCode;
        if (violation.isPresent()) {
            final String violationMessage = violation.get().getMessage();
            final String property = ErrorUtil.getPropertyName(violation.get().getPropertyPath());
            errorMessage = property + " " + messageSourceUtil.getMessage(violationMessage);
            errorCode = messageSourceUtil.getErrorCode(violationMessage, CommonErrorCode.PARAMETER_INVALID);
        } else {
            errorMessage = ex.getMessage();
            errorCode = CommonErrorCode.PARAMETER_INVALID;
        }
        addErrorLog(BAD_REQUEST, errorCode, errorMessage, "ConstraintViolationException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                errorCode,
                errorMessage);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<CommonErrorResponse> handleClientException(ClientException ex) {
        final HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatus());
        addErrorLog(httpStatus, ex.getErrorCode(), ex.getMessage(), "ClientException");
        final CommonErrorResponse errorResponse = new CommonErrorResponse(
                ex.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FeignException.class)
    public CommonErrorResponse handleFeignException(FeignException ex) {
        addErrorLog(HttpStatus.resolve(ex.status()), CommonErrorCode.UNEXPECTED_INTERNAL_ERROR, ex.getMessage(), ex);
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.UNEXPECTED_INTERNAL_ERROR,
                ex.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CommonException.class)
    public CommonErrorResponse handleCommonException(CommonException ex) {
        addErrorLog(INTERNAL_SERVER_ERROR, ex.getErrorCode(), ex.getMessage(), ex);
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonErrorResponse handleAll(Exception ex) {
        addErrorLog(INTERNAL_SERVER_ERROR, CommonErrorCode.UNEXPECTED_INTERNAL_ERROR, ex.getMessage(), ex);
        final String errMsg = "Unexpected internal server error";
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.UNEXPECTED_INTERNAL_ERROR,
                errMsg/*ex.getMessage()*/);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        final Optional<FieldError> fieldError = ex.getBindingResult().getFieldErrors().stream().findAny();
        String errorMessage;
        String errorCode;
        if (fieldError.isPresent()) {
            final String fieldName = fieldError.get().getField();
            final String fieldMessage = fieldError.get().getDefaultMessage();
            errorMessage = fieldName + " " + messageSourceUtil.getMessage(fieldMessage);
            errorCode = messageSourceUtil.getErrorCode(fieldMessage, CommonErrorCode.REQUEST_INVALID);
        } else {
            errorMessage = ex.getMessage();
            errorCode = CommonErrorCode.REQUEST_INVALID;
        }
        final String errLogMessage = String.join(System.lineSeparator(),
                errorMessage,
                "Request body: " + webUtil.getRequestBody());
        addErrorLog(status, errorCode, errLogMessage, "MethodArgumentNotValidException");
        final CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                errorCode,
                errorMessage);
        return new ResponseEntity<>(commonErrorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        final String error = ex.getParameterName() + " parameter is missing";
        addErrorLog(status, CommonErrorCode.PARAMETER_INVALID, error, "MissingServletRequestParameterException");
        final CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.PARAMETER_INVALID,
                error);
        return new ResponseEntity<>(commonErrorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        addErrorLog(status, CommonErrorCode.PARAMETER_INVALID, ex.getMessage(), "MissingServletRequestPartException");
        final CommonErrorResponse errorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.PARAMETER_INVALID,
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        addErrorLog(status, CommonErrorCode.PARAMETER_INVALID, ex.getMessage(), "HttpMediaTypeNotSupportedException");
        final CommonErrorResponse errorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.PARAMETER_INVALID,
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        final String errLogMessage = String.join(System.lineSeparator(),
                ex.getMessage(),
                "Request body: " + webUtil.getRequestBody());
        final String error = "Request not readable";
        addErrorLog(status, CommonErrorCode.REQUEST_INVALID, errLogMessage, "HttpMessageNotReadableException");
        final CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.REQUEST_INVALID,
                error/*ex.getMessage()*/);
        return new ResponseEntity<>(commonErrorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        addErrorLog(status, CommonErrorCode.PARAMETER_INVALID, ex.getMessage(), "ServletRequestBindingException");
        final CommonErrorResponse errorResponse = new CommonErrorResponse(
                webUtil.getRequestId(),
                CommonErrorCode.PARAMETER_INVALID,
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }


    //*** Logging ***//

    protected void addErrorLog(HttpStatus httpStatus, String errorCode, String errorMessage, Throwable ex) {
        log.error("[Error] | Status: {} | Code: {} | Type: {} | Path: {} | Elapsed time: {} ms | Message: {}",
                httpStatus, errorCode, ex.getClass().getTypeName(), webUtil.getRequestUri(),
                v("elapsed_time", webUtil.getElapsedTime()), errorMessage, ex);
    }

    protected void addErrorLog(HttpStatus httpStatus, String errorCode, String errorMessage, String exceptionType) {
        log.error("[Error] | Status: {} | Code: {} | Type: {} | Path: {} | Elapsed time: {} ms | Message: {}",
                httpStatus, errorCode, exceptionType, webUtil.getRequestUri(),
                v("elapsed_time", webUtil.getElapsedTime()), errorMessage);
    }

}
