package com.kaptureevents.KaptureEvents.handler;

import java.util.Base64;

public class ImageUtilsHandler {
    private static final int MAX_IMAGE_SIZE_BYTES = 64 * 1024; // 64KB

    public static String encodeImage(byte[] imageData) throws IllegalArgumentException {
        if (imageData.length > MAX_IMAGE_SIZE_BYTES) {
            throw new IllegalArgumentException("Image size exceeds 64KB limit");
        }
        return Base64.getEncoder().encodeToString(imageData);
    }

    public static byte[] decodeImage(String imageBase64) throws IllegalArgumentException {
        return Base64.getDecoder().decode(imageBase64);
    }
}


