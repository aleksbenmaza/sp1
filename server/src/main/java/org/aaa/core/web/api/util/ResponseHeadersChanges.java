package org.aaa.core.web.api.util;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexandremasanes on 26/10/2017.
 */
public class ResponseHeadersChanges extends HttpEntity<Void> {

    public static class Builder {
        boolean done;
        HashMap<String, List<String>> headers;

        private Builder() {}

        public Builder put(String headerName, String headerValue, String... headerValues) {
            if(done)
                throw new RuntimeException();
            headers.put(headerName, toList(headerValue, headerValues));
            return this;
        }

        public ResponseHeadersChanges build() {
            done = true;
            return new ResponseHeadersChanges(headers);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public HttpHeaders getHeaders() {
        return new HttpHeaders();
    }

    public ResponseHeadersChanges(String headerName, String headerValue, String... headerValues) {
        super.getHeaders().put(headerName, toList(headerValue, headerValues));
    }

    private ResponseHeadersChanges(HashMap<String, List<String>> headers) {
        super.getHeaders().putAll(headers);
    }

    private static List<String> toList(String headerValue, String... headerValues) {
        ArrayUtils.add(headerValues, 0, headerValue);
        return Arrays.asList(headerValues);
    }
}
