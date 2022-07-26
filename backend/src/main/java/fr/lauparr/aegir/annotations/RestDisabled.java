package fr.lauparr.aegir.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RestDisabled {

  String condition() default "";

}
