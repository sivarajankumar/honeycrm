package honeycrm.server.domain.decoration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import honeycrm.server.domain.AbstractEntity;

@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	Class<? extends AbstractEntity> value();
}
