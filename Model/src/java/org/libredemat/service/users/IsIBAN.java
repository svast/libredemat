package org.libredemat.service.users;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.oval.configuration.annotation.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(checkWith = IsIBANCheck.class)
public @interface IsIBAN {

    String message() default "";

    String[] profiles() default {};

    String when() default "";
}
