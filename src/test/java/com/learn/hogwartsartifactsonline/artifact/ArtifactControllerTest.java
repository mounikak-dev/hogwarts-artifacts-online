package com.learn.hogwartsartifactsonline.artifact;

import com.learn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import com.learn.hogwartsartifactsonline.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904192");
        a1.setName("Invisibility Cloak");
        a1.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904183");
        a2.setName("Deluminator");
        a2.setDescription("An Deluminator is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904592");
        a3.setName("Stone");
        a3.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a3.setImageUrl("ImageUrl");

        artifacts.add(a1);
        artifacts.add(a2);
        artifacts.add(a3);
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void testFindArtifactByIdSuccess() throws Exception {
//
//        given(this.artifactService.findById("1250808601744904183")).willReturn(this.artifacts.get(1));
//
//        this.mockMvc.perform(get("/api/v1/artifacts/1250808601744904183")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1250808601744904183"));
//
//    }

    @Test
    void testAddArtifactSuccess() {
        ArtifactDto artifactDto = new ArtifactDto(null,
                "Remembrall",
                "remmebers all",
                "imageurl", null);

           String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904197");
        a3.setName("Remembrall");
        a3.setDescription("remmebers all");
        a3.setImageUrl("imageurl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(a3);


    }
}