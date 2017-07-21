package app.core.web.logic.controller.annotation;

import java.lang.annotation.*;

/**
 * Created by alexandremasanes on 29/04/2017.
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface PostHandler {

    @Target(value = ElementType.METHOD)
    @Retention(value = RetentionPolicy.RUNTIME)
    @Documented
    @interface Ignored {

    }
}
