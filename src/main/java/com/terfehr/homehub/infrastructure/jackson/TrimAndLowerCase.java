package com.terfehr.homehub.infrastructure.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.lang.annotation.*;

/**
 * Annotation to make use of TrimAndLowerCaseStringDeserializer to convert a String to lower-case and trim it of leading and following white spaces.
 * Mainly used for normalizing requests right after their deserialization from JSON by Jackson.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = TrimAndLowerCaseStringDeserializer.class)
public @interface TrimAndLowerCase {
}
