package honeycrm.server.domain.decoration;

import honeycrm.server.domain.AbstractEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	Class<? extends AbstractEntity> value();
}
