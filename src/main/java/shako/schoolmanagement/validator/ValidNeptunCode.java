package shako.schoolmanagement.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NeptunCodeValidator.class)
@ReportAsSingleViolation
public @interface ValidNeptunCode {



    String message() default "Invalid Neptun Code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
