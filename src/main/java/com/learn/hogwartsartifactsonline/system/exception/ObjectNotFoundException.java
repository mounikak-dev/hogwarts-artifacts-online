package com.learn.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String ObjectName, String id) {
        super("could not find " + ObjectName + " with id " + id);
    }

    public ObjectNotFoundException(String ObjectName, Integer id) {
        super("could not find " + ObjectName + " with id " + id);
    }
}
