package com.learn.hogwartsartifactsonline.artifact;

import com.learn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import com.learn.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.learn.hogwartsartifactsonline.client.ai.chat.*;
import com.learn.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

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

    public String summarizeArtifacts(List<ArtifactDto> artifacts){
       return this.chatClient.generate(artifacts);
    }

    public Page<Artifact> findByCriteria(Map<String, String> criteria, Pageable pageable) {
        Specification<Artifact> spec = Specification.unrestricted();

        if(StringUtils.hasLength(criteria.get("id"))){
            spec = spec.and(ArtifactSpecs.hasId(criteria.get("id")));
        }
        if(StringUtils.hasLength(criteria.get("name"))){
            spec = spec.and(ArtifactSpecs.containsName(criteria.get("name")));
        }
        if(StringUtils.hasLength(criteria.get("description"))){
            spec = spec.and(ArtifactSpecs.containsDescription(criteria.get("description")));
        }
        if(StringUtils.hasLength(criteria.get("ownerName"))){
            spec = spec.and(ArtifactSpecs.hasOwnerName(criteria.get("ownerName")));
        }

        return this.artifactRepository.findAll(spec, pageable);
    }
}
