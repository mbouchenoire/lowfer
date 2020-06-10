package org.lowfer.web.rest.vm;

public final class EncodedArchitectureView {

    private final String encoded;
    private final String error;

    private EncodedArchitectureView(String encoded, String error) {
        this.encoded = encoded;
        this.error = error;
    }

    public static EncodedArchitectureView ok(String encoded) {
        return new EncodedArchitectureView(encoded, null);
    }

    public static EncodedArchitectureView error(Throwable throwable) {
        return new EncodedArchitectureView(null, throwable.getMessage());
    }

    public String getEncoded() {
        return encoded;
    }

    public String getError() {
        return error;
    }
}
