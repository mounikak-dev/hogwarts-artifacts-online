package com.learn.hogwartsartifactsonline.client.ai.chat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {
    private List<Step> steps;

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Step {
        private String type;
        private List<Content> content;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public List<Content> getContent() { return content; }
        public void setContent(List<Content> content) { this.content = content; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private String type;
        private String text;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
