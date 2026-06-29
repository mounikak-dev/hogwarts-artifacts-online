package com.learn.hogwartsartifactsonline.artifact;

import com.learn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import com.learn.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.learn.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.learn.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //given-arrange inputs and targets
        /*
         "id": "1250808601744904192",
    "name": "Invisibility Cloak",
    "description": "An invisibility cloak is used to make the wearer invisible.",
    "imageUrl": "ImageUrl",
         */

        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry");

        a.setOwner(w);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        //when. Act on the target behavior.

        Artifact retturnedArtifact = artifactService.findById("1250808601744904192");
        //then. Assert expected outcomes.

        assertThat(retturnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(retturnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(retturnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(retturnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

//    @Test
//    void testFindByIdNotFound() {
//        given(artifactRepository.findById(Mockito.anyString())).willReturn(Optional.empty());
//
//        Throwable thrown = catchThrowable(() -> {
//            Artifact retturnedArtifact = artifactService.findById("1250808601744904192");
//        });
//
//        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("could not find artifact with id 1250808601744904192");
//        verify(artifactRepository, times(1)).findById("1250808601744904192");
//
//    }

    @Test
    void testSaveSuccess() {
        Artifact artifact = new Artifact();
        artifact.setName("Artifact 3");
        artifact.setDescription("desc/....");
        artifact.setImageUrl("imageurl....");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(artifact)).willReturn(artifact);

        Artifact savedArtifact = artifactService.save(artifact);
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(artifact.getName());

        verify(artifactRepository, times(1)).save(artifact);
    }



}