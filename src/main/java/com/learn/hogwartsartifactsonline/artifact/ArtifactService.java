package com.learn.hogwartsartifactsonline.artifact;

import com.learn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import com.learn.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.learn.hogwartsartifactsonline.client.ai.chat.ChatClient;
import com.learn.hogwartsartifactsonline.client.ai.chat.ChatRequest;
import com.learn.hogwartsartifactsonline.client.ai.chat.ChatResponse;
import com.learn.hogwartsartifactsonline.client.ai.chat.InputData;
import com.learn.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    public final IdWorker idWorker;

    private final ChatClient chatClient;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker, ChatClient chatClient) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
        this.chatClient = chatClient;
    }

    @Observed(name="artifact", contextualName = "findByIdService")
    public Artifact findById(String artifactId) {
        return this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("Artifact",artifactId));
    }

    @Timed("findAllArtifactsService.time")
    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact artifact) {
        return this.artifactRepository.findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(artifact.getName());
                    oldArtifact.setDescription(artifact.getDescription());
                    oldArtifact.setImageUrl(artifact.getImageUrl());
                    return this.artifactRepository.save(oldArtifact);
                })
                .orElseThrow(() -> new ObjectNotFoundException("Artifact",artifactId));

    }

    public void delete(String artifactId) {
        this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("Artifact",artifactId));
        this.artifactRepository.deleteById(artifactId);
    }

    public String summarizeArtifacts(List<ArtifactDto> artifactDtos){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = objectMapper.writeValueAsString(artifactDtos);

        List<InputData> inputs = List.of(
                new InputData("user_input", "your task is to generate short summary of given JSON array in at most 100 words. The summary must include number of artifacts, description of each artifact, ownership information. Do not mention the summary is from given JSON array."),
                new InputData("user_input", jsonArray)
        );

        ChatRequest chatRequest = new ChatRequest("gemini-3.6-flash", inputs);

        ChatResponse chatResponse = this.chatClient.generate(chatRequest);
        return chatResponse.contentData().get(0).text();
    }
}
