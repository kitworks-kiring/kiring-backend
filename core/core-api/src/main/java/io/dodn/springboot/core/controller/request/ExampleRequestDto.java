package io.dodn.springboot.core.controller.request;

import io.dodn.springboot.core.domain.ExampleData;

public record ExampleRequestDto(String data) {
    public ExampleData toExampleData() {
        return new ExampleData(data, data);
    }
}
