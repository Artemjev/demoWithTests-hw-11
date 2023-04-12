package com.example.demowithtests.util.validation.annotation.constraints;

import com.example.demowithtests.util.validation.validator.PhotoFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//!!!!!!!!!!!!!!!!!!!    Currently not in use! !!!!!!!!!!!!!

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhotoFileValidator.class)
public @interface PhotoFileConstraint {

    String value() default "image/jpeg";

    //    int maxFileSize() default 1024;

    String message() default "Wrong file format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

