package honeycrm.server.domain.decoration.fields;

import honeycrm.server.domain.AbstractEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldTableAnnotation {
	Class<? extends AbstractEntity> value();
}
