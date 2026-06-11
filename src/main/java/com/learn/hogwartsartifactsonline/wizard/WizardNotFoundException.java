package com.learn.hogwartsartifactsonline.wizard;


public class WizardNotFoundException extends RuntimeException {

    public WizardNotFoundException(Integer id) {
        super("could not find wizard with id:" + id);
    }
}
