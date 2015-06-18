package annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface NoArgsConstructor {
}
