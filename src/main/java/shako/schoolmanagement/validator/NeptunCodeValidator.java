package shako.schoolmanagement.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NeptunCodeValidator implements ConstraintValidator<ValidNeptunCode, String> {
    @Override
    public void initialize(ValidNeptunCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String neptunCode, ConstraintValidatorContext context) {
        if (neptunCode == null) {
            return true;
        }
        return neptunCode.matches("[a-zA-Z0-9]{6}");
    }
}
