package honeycrm.server.domain.decoration.fields;

import honeycrm.server.domain.AbstractEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldRelateAnnotation {
	Class<? extends AbstractEntity> value();
}
