package com.learn.hogwartsartifactsonline.client.ai.chat;

import com.learn.hogwartsartifactsonline.artifact.dto.ArtifactDto;

import java.util.List;

public interface ChatClient {
    String generate(List<ArtifactDto> artifacts);
}
