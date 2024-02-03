package com.homelyassist.utils;

import com.homelyassist.exception.MemberAlreadyExistException;
import com.homelyassist.exception.MemberRegistrationValidationFailed;

import java.util.Arrays;
import java.util.List;

public class ErrorUtils {

    private static final List<Class<? extends Exception>> EXCEPTION_CLASSES = Arrays.asList(
            MemberRegistrationValidationFailed.class,
            MemberAlreadyExistException.class
    );

    public static String getErrorMessage(Exception e) {

        if(EXCEPTION_CLASSES.stream().noneMatch(exceptionClass -> exceptionClass.isInstance(e)))
            return AppConstant.ERROR.INTERNAL_SERVER_ERROR;

        return e.getMessage();
    }
}
