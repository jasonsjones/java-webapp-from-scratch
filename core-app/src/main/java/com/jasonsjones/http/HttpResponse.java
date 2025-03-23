package com.jasonsjones.http;

public class HttpResponse extends HttpMessage {

    private static final String CRLF = "\r\n";

    private HttpVersion version;
    private String reasonPhrase = null;

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public String getReasonPhrase() {
        if (reasonPhrase == null && this.statusCode != null) {
            return this.statusCode.MESSAGE;
        }
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public byte[] getResponseBytes() {
        StringBuilder resStringBuilder = new StringBuilder();
        resStringBuilder.append(this.version.LITERAL)
            .append(" ")
            .append(statusCode.STATUS_CODE)
            .append(" ")
            .append(getReasonPhrase())
            .append(CRLF);

        for (String headerName: getHeaderNames()) {
            resStringBuilder.append(headerName)
                .append(": ")
                .append(getHeader(headerName))
                .append(CRLF);
        }

        // add CRLF between headers and body
        resStringBuilder.append(CRLF);

        byte[] responseBytes = resStringBuilder.toString().getBytes();

        // return the bytes of the response if there is no body
        if (getMessageBody().length == 0) {
            return responseBytes;
        }

        // create byte[] to hold the headers and the response body
        byte[] reesponseWithBody = new byte[responseBytes.length + getMessageBody().length];
        // copy headers into the responseWithBody byte array
        System.arraycopy(responseBytes, 0, reesponseWithBody, 0, responseBytes.length);
        // append the response body into the responseWithBody byte array
        System.arraycopy(getMessageBody(), 0, reesponseWithBody, responseBytes.length, getMessageBody().length);
        
        return reesponseWithBody;
    }

    public static class Builder {
        private HttpResponse response;

        public Builder() {
            response = new HttpResponse();
        }

        public Builder withVersion(HttpVersion version) {
            response.setVersion(version);
            return this;
        }

        public Builder withStatusCode(HttpStatusCode statusCode) {
            response.setStatusCode(statusCode);
            return this;
        }

        public Builder withReasonPhrase(String reasonPhrase) {
            response.setReasonPhrase(reasonPhrase);
            return this;
        }

        public Builder withHeader(String name, String value) {
            response.addHeader(name, value);
            return this;
        }

        public Builder withBody(byte[] body) {
            response.setMessageBody(body);
            return this;
        }

        public HttpResponse build() {
            return response;
        }
        
    }

}