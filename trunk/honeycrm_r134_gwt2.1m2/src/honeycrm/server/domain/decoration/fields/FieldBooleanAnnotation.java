package honeycrm.server.domain.decoration.fields;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldBooleanAnnotation {
	boolean defaultValue() default false;
}
