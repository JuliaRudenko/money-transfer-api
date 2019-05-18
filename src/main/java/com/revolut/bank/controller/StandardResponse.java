package com.revolut.bank.controller;

import com.google.gson.JsonElement;

public class StandardResponse {

    private String message;
    private JsonElement data;

    StandardResponse(String message) {
        this.message = message;
    }

    StandardResponse(JsonElement data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}