package com.tbfp.teamplannerbe.domain.distributeLock;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    String key(); // (1)

    TimeUnit timeUnit() default TimeUnit.SECONDS; // (2)

    long waitTime() default 5L; // (3)

    long leaseTime() default 15L; // (4)
}