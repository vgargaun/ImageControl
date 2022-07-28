package com.crossinx;

import com.crossinx.UI.Image;
import com.crossinx.UI.MainForm;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MainManager {
    private static MainManager instance;

    @Getter
    private MainForm mainForm;
    private Image image = new Image();

    public static MainManager getInstance() {
        if (instance == null) {
            instance = new MainManager();
        }
        return instance;
    }

    public Image getImage() {
        return image;
    }

    public void resetAll() {
        setActivateTab(0);
    }

    void start() {
        mainForm = new MainForm();
        mainForm.setSize(1024, 900);
        mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainForm.setVisible(true);
    }

    public void initDocument() {

        JFileChooser fc = new JFileChooser();

        fc.setFileFilter(new FileNameExtensionFilter("Images file", new String[]{"jpg", "jpeg", "png"}));
        fc.setMultiSelectionEnabled(true);
        fc.setCurrentDirectory(new File("C:\\Users\\veaceslav.gargaun\\Downloads"));
        fc.showDialog(null, "Open");
        File[] files = fc.getSelectedFiles();
        try {
            Image imagesRecognize;
            Image image = convertPdfToImagine(files[0]);
            if(Optional.ofNullable(image).isPresent()){
                imagesRecognize = image;
            } else {
                imagesRecognize = new Image(ImageIO.read(files[0]));
            }
            imagesRecognize.setQuality(1);
            this.image = imagesRecognize;
            initRecognizePanel();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Image convertPdfToImagine(File file) throws IOException {
        try {
            PDDocument document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(
                        page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(
                        bim, String.format("C:\\Users\\veaceslav.gargaun\\Downloads/pdf-image.%s", "png"), 300);
            }
            document.close();
        } catch (Exception e){
            return null;
        }
        return new Image(ImageIO.read(new File("C:\\Users\\veaceslav.gargaun\\Downloads/pdf-image.png")));

    }


    private void initRecognizePanel() {
        this.mainForm.initRecognize(this.image);
        setActivateTab(0);
    }

    public void setActivateTab(int indexTab) {
        this.mainForm.selectTab(indexTab);
    }



}
