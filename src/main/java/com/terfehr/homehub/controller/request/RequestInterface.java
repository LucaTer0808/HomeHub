package com.terfehr.homehub.controller.request;

/**
 * Interface for a request object. It has to implement a validate method as well as a normalize method.
 */
public interface RequestInterface {

    boolean validate();
}
