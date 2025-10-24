package com.terfehr.homehub.infrastructure.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.lang.annotation.*;

/**
 * Annotation to make user of the TrimmedStringDeserializer to trim inputs directly after they were deserialized from the JSON from the Request by Jackson.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = TrimStringDeserializer.class)
public @interface Trim {
}
