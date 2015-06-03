package my.thereisnospoon.webm.entities.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=UniqueUsernameValidator.class)
public @interface UniqueUsername {

	String message() default "Username is already taken";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
