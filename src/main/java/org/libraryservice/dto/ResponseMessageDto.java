package org.libraryservice.dto;

public class ResponseMessageDto {
    private String message;

    private ResponseMessageDto() {
    }

    public ResponseMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
