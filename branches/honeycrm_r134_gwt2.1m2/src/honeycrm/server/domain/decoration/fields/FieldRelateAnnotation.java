package honeycrm.server.domain.decoration.fields;

import honeycrm.server.domain.Bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldRelateAnnotation {
	Class<? extends Bean> value();
}
