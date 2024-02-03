package com.homelyassist.exception;

public class MemberRegistrationValidationFailed extends RuntimeException {

    public MemberRegistrationValidationFailed() {
        super();
    }

    public MemberRegistrationValidationFailed(String fieldName) {
        super(String.format("field is not nullable: %s", fieldName));
    }

    public MemberRegistrationValidationFailed(String fieldName, String value) {
        super(String.format("invalid value: %s for field: %s", value, fieldName));
    }
}
