package dev.fadisarwat.bookstore.rules;

import dev.fadisarwat.bookstore.annotations.Unique;
import dev.fadisarwat.bookstore.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UniqueConstraintValidator implements ConstraintValidator<Unique, String> {

    private String column;
    private String table;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void initialize(Unique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.column = constraintAnnotation.value();
        this.table = constraintAnnotation.table();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return validationService.isUnique(table, column, value);
    }
}
