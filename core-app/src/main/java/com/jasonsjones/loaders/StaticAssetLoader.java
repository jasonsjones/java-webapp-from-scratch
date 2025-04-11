package com.jasonsjones.loaders;

import java.io.IOException;
import java.io.InputStream;

public class StaticAssetLoader {

    public static byte[] loadStaticAsset(String assetFile) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(assetFile)) {
            if (inputStream == null) {
                throw new IOException("Static asset file not found: " + assetFile);
            }
            return inputStream.readAllBytes();
        }
    }
}
