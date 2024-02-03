package com.homelyassist.exception;

public class MemberAlreadyExistException extends RuntimeException {

    public MemberAlreadyExistException(String phoneNumber) {
        super(String.format("user already exist with phone number: %s", phoneNumber));
    }
}
