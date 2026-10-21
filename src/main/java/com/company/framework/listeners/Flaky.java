package com.company.framework.listeners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark specific test methods as flaky, allowing the
 * {@link RetryAnalyzer} to selectively retry them on failure.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Flaky {
    /**
     * The maximum number of retry attempts for the annotated test method.
     * Defaults to 1 retry.
     *
     * @return maximum retry count
     */
    int maxRetry() default 1;
}
