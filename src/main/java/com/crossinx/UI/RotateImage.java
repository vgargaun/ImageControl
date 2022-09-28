package com.crossinx.UI;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class RotateImage {
    Image image;

    public RotateImage(Image image) {
        this.image = image;
    }

    public BufferedImage rotateImage(double grade) {
        BufferedImage bufferedImage = image.getBufferedImage();
        final double rads = Math.toRadians(grade);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(bufferedImage.getWidth() * cos + bufferedImage.getHeight() * sin);
        final int h = (int) Math.floor(bufferedImage.getHeight() * cos + bufferedImage.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, bufferedImage.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads, 0, 0);
        at.translate(-bufferedImage.getWidth() / 2, -bufferedImage.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(bufferedImage, rotatedImage);
        return rotatedImage;
    }
}
