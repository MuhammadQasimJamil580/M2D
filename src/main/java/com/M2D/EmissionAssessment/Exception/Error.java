package com.M2D.EmissionAssessment.Exception;



import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class Error {
    public String timeStamp;
    public String error;
    public String status;
    public String message;
    public String uri;

        public Error()
        {
            this.timeStamp = Instant.now().toString();
        }

        public Error(String error, String status, String message, String uri) {
        this.timeStamp = Instant.now().toString();
        this.error = error;
        this.status = status;
        this.message = message;
        this.uri=uri;
    }
}
