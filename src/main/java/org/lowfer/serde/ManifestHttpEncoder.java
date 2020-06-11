/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
