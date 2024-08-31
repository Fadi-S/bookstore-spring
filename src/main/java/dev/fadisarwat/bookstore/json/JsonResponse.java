package dev.fadisarwat.bookstore.json;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class JsonResponse {
    private int status;
    private String message;
    private JSONObject messageJson;
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

    public JsonResponse(int status, JSONObject message) {
        this.status = status;
        this.messageJson = message;
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

    public Object getMessage() {
        if(this.messageJson != null) {
            return this.messageJson.toMap();
        }

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(JSONObject message) {
        this.messageJson = message;
    }

    public void setErrors(JSONObject errors) {
        if (this.messageJson == null) {
            this.messageJson = new JSONObject();
        }

        this.messageJson.put("errors", errors);

        this.setStatus(400);
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

    public Map<String, Object> toMap() {
        JSONObject json = new JSONObject();
        json.put("status", this.status);
        json.put("message", this.getMessage());
        json.put("timestamp", this.timestamp);
        json.put("httpStatus", this.getHttpStatus());

        return json.toMap();
    }
}
