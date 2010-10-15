package honeycrm.server.domain.decoration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Attach this annotation to entities whenever you want to hide them in the user interface.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Hidden {
}
