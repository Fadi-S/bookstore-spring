package dev.fadisarwat.bookstore.json;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonResponse {
    private int status;
    private String message;
    private long timestamp;

    public JsonResponse() {
        this.timestamp = System.currentTimeMillis();
        this.status = 200;
    }

    public JsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getHttpStatus() {
        if (status == 0) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.valueOf(status);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setHttpStatus(HttpStatus status) {
        this.status = status.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ResponseEntity<JsonResponse> get() {
        return new ResponseEntity<>(this, this.getHttpStatus());
    }
}
