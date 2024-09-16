package dev.fadisarwat.bookstore.annotations;

import dev.fadisarwat.bookstore.rules.UniqueConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    public String value() default "";
    public String table() default "";

    public String message() default "is not unique";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
