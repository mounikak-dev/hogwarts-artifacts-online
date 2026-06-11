package com.learn.hogwartsartifactsonline.artifact;

public class ArtifactNotFoundException extends RuntimeException {

    public ArtifactNotFoundException(String id) {
        super("could not find artifact with id " + id);
    }
}
