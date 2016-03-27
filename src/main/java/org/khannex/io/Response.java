package org.khannex.io;

public class Response {
    private String errorCode;
    private String errorText;
    private String result;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode( String errorCode ) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText( String errorText ) {
        this.errorText = errorText;
    }

    public String getResult() {
        return result;
    }

    public void setResult( String result ) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Response [errorCode=" + errorCode + ", errorText=" + errorText + ", result=" + result + "]";
    }

}
