package ru.otus.questionnaire.mettaannotaions;

import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles
public @interface TestProfile {
    @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles") String[] activeProfiles() default {"test"};

}
