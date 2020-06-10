package org.lowfer.serde;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class ManifestHttpEncoder implements ManifestEncoder {

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private static final Charset ENCODING_CHARSET = StandardCharsets.UTF_8;

    @Override
    public String encode(String clear) {
        final byte[] encodedBytes = ENCODER.encode(clear.getBytes(ENCODING_CHARSET));
        return new String(encodedBytes, ENCODING_CHARSET);
    }

    @Override
    public String decode(String encoded) {
        final byte[] decodedBytes = DECODER.decode(encoded);
        return new String(decodedBytes, ENCODING_CHARSET);
    }
}
