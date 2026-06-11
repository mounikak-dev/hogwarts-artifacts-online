package com.learn.hogwartsartifactsonline.artifact.converter;

import com.learn.hogwartsartifactsonline.artifact.Artifact;
import com.learn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {

    @Override
    public Artifact convert(ArtifactDto source) {
        Artifact artifact = new Artifact();
        artifact.setId(source.id());
        artifact.setDescription(source.description());
        artifact.setName(source.name());
        artifact.setImageUrl(source.imageUrl());
        return artifact;
    }
}
