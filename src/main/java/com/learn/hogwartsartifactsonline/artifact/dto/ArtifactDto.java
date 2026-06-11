package com.learn.hogwartsartifactsonline.artifact.dto;

import com.learn.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(String id,
                         @NotEmpty(message = "Name is required") String name,
                          @NotEmpty(message = "description is required") String description,
                          @NotEmpty(message = "imageUrl is required") String imageUrl,
                         WizardDto owner) {
}
