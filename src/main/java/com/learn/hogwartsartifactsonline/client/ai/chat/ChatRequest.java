package com.learn.hogwartsartifactsonline.client.ai.chat;

import java.util.List;

public record ChatRequest(String model, List<InputData> inputs) {
}
