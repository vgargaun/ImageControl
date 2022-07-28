package com.crossinx.UI;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Image {
    private BufferedImage originalImage;
    private BufferedImage outPutImage;

    private float[] compressionLevels = {0.01f, 0.02f, 0.04f, 0.06f, 0.08f, 0.09f, 0.1f};

    public Image(BufferedImage image) {
        this.originalImage = image;
        try {
            restoreToOriginalImage();
        } catch (IOException e) {
        }
    }

    public Image() {
        this.originalImage = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
        this.outPutImage = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public void saveImage(File path) throws IOException {
        ImageIO.write(this.outPutImage, "jpg", path);
    }

    public void setQuality(float levelCompression) {

        float quality = 1.0f;
        try {
            quality = levelCompression;

        } catch (ArrayIndexOutOfBoundsException e) {
        }

        Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = (ImageWriter) iter.next();

        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(quality);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios;
        try {
            ios = ImageIO.createImageOutputStream(baos);
        } catch (IOException e) {
            return;
        }

        writer.setOutput(ios);

        BufferedImage originalImage = this.originalImage;

        IIOImage image = new IIOImage(originalImage, null, null);
        try {
            writer.write(null, image, iwp);
        } catch (IOException e) {
        }
        byte[] data = baos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try {
            this.outPutImage = ImageIO.read(bis);
        } catch (IOException e) {

        }
        writer.dispose();

    }

    private void restoreToOriginalImage() throws IOException {
        BufferedImage bImage = this.originalImage;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        byte[] data = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        this.outPutImage = ImageIO.read(bis);

    }

    public BufferedImage getBufferedImage() {
        return this.originalImage;
    }

    public void rotate(double angle) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = this.outPutImage.getWidth();
        int h = this.outPutImage.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);
        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(this.outPutImage, null, 0, 0);
        g2d.dispose();
        this.outPutImage = rotated;
    }
}
