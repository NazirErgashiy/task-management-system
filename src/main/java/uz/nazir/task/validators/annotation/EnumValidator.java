package uz.nazir.task.validators.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        annotation = constraintAnnotation;
    }


    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        String[] availableValues = annotation.value();
        for (String availableValue : availableValues) {
            if (Objects.equals(availableValue, string)) {
                return true;
            }
        }
        return false;
    }
}
