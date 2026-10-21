package com.company.framework.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG RetryAnalyzer implementation that selectively retries only tests annotated with {@link Flaky}.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int count = 0;

    @Override
    public boolean retry(ITestResult result) {
        Method testMethod = result.getMethod().getConstructorOrMethod().getMethod();
        if (testMethod != null && testMethod.isAnnotationPresent(Flaky.class)) {
            Flaky flaky = testMethod.getAnnotation(Flaky.class);
            int maxRetry = flaky.maxRetry();
            if (count < maxRetry) {
                count++;
                logger.warn("Retrying flaky test '{}' (Attempt {}/{}) due to failure: {}",
                        result.getName(), count, maxRetry,
                        result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error");
                return true;
            }
        }
        return false;
    }

    /**
     * TestNG Annotation Transformer that attaches {@link RetryAnalyzer} only to test methods with {@link Flaky}.
     */
    public static class AnnotationTransformer implements IAnnotationTransformer {
        @Override
        public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
            if (testMethod != null && testMethod.isAnnotationPresent(Flaky.class)) {
                annotation.setRetryAnalyzer(RetryAnalyzer.class);
            }
        }
    }
}
