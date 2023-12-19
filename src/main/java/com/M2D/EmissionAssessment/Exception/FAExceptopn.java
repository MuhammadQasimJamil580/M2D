package com.M2D.EmissionAssessment.Exception;



public class FAExceptopn extends Exception {

    private static final long serialVersionUID = 1L;
    private String errorCode;
    private String message;

    public FAExceptopn() {
        super();
    }

    public FAExceptopn(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FAExceptopn(final String message) {
        super(message);
    }

//    public FAExceptopn(final String message, final String code) {
//        this. errorCode = code;
//        this. message = message;
//        new FAExceptopn(errorCode,message);
//    }
    public FAExceptopn(String code, String message){
        this.errorCode=code;
        this.message=message;
    }
    public FAExceptopn(final Throwable cause) {
        super(cause);
    }

//    public FAExceptopn(PaymentServiceErrorsEnum userManagementServiceErrorsEnum) {
//        this.errorCode=userManagementServiceErrorsEnum.getCode();
//        this.message=userManagementServiceErrorsEnum.getValue();
//    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
