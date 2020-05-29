package com.ddanilyuk.DeadlinesServer.extensions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder(value = {"type", "code", "message"})

/**
 * Клас для виводу помилки або успіху
 */

public class RestMessage {

    @JsonProperty("code")
    int code;

    @JsonProperty("type")
    String type;

    @JsonProperty("message")
    String message;


    public RestMessage() {
        super();
    }

    public RestMessage(String type, int code, String message) {

        this.code = code;
        this.type = type;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}