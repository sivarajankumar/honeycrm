package honeycrm.server.domain.decoration;

import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
public @interface Quicksearchable {
	String[] value();
}
