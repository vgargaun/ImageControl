package com.crossinx.ocr;


import lombok.Getter;
import lombok.Setter;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.global.lept;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.tesseract.Tesseract;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Getter
@Setter
public class OcrWorker {
    private static TessBaseAPI api;
    static {
        try {
            api = initiateTessBaseApi();
        } catch (Exception e) {
        }
    }
    public static String processOcr(BufferedImage bufimage, Rectangle r){
        Tesseract tesseract = new Tesseract();
//        tesseract.setDatapath("D:\\workspace\\FxTests\\tesseract-4.1.0\\tessdata");
//        tesseract.setLanguage("eng");
//        tesseract.setPageSegMode(1);
//        tesseract.setOcrEngineMode(1);
        bufimage = bufimage.getSubimage((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());

        try {
            String text = "";
            File a = new File("temp.png");
            ImageIO.write(bufimage, "png", a);

            try {
                text = processOcr(a.getAbsolutePath());
            } catch (Exception e) {
            }

            return text;
        } catch (IOException e) {
            return "ERROR";
        }

    }

    private static String processOcr(String file) throws Exception {
        if (api == null) {
            return "";
        }
        PIX image = null;
        try {
            image = lept.pixRead(file);
            api.SetImage(image);
            String string = api.GetUTF8Text().getString("UTF-8");
            return string;
        } catch (UnsupportedEncodingException e) {
            throw new Exception("charset", e);
        } finally {
            if (image != null) {
                lept.pixDestroy(image);
            }
        }
    }

    private static TessBaseAPI initiateTessBaseApi() throws Exception {
        TessBaseAPI api = new TessBaseAPI();
        if (api.Init("D:\\workspace\\FxTests\\tesseract-4.1.0\\tessdata", "eng") != 0) {
            throw new Exception("Could not initialize tesseract.");
        }
        return api;
    }
}
