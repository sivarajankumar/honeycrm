package honeycrm.server.domain.decoration.fields;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO support specification of default width 
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldCurrencyAnnotation {
	String value(); // defines the default value
}
