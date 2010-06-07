package honeycrm.client.dto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RelatesTo {
	Class<? extends AbstractDto> value();
}
