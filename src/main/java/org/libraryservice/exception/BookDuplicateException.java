package org.libraryservice.exception;

public class BookDuplicateException extends RuntimeException {
    public BookDuplicateException(String message) {
        super(message);
    }
}
