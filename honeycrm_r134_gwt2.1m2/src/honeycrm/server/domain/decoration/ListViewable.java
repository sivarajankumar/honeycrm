package honeycrm.server.domain.decoration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ListViewable {
	String[] value();
}
