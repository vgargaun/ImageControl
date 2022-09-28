package com.crossinx.UI;


import com.crossinx.MainManager;
import com.crossinx.UI.openCV.OpenCVService;
import gov.nasa.worldwind.symbology.milstd2525.graphics.EchelonSymbol;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SelectImageControllerPanel extends JPanel {
    private JButton selectFiles = new JButton();
    private JButton adjustmentButton = new JButton();
    private JButton resetButton = new JButton();


    SelectImageControllerPanel() {
        super();
        this.setLayout(new GridLayout(1, 40, 10, 10));
        this.add(this.selectFiles);
        this.add(this.adjustmentButton);
        this.add(this.resetButton);
        resetCaptions();
        //
        init();
    }


    private void init() {

        this.selectFiles.addActionListener(e -> {
                    activateController();
                }
        );
        
        this.adjustmentButton.addActionListener(e ->
                adjustmentImagine()
        );
        
        this.resetButton.addActionListener(e ->
                MainManager.getInstance().resetAll()
        );
    }

    private void adjustmentImagine() {
        OpenCVService openCVService = new OpenCVService();
        openCVService.adjustmentImagine(MainManager.getInstance().getImage());

    }


    private void rotateImage(Image image){
        RotateImage rotateImage = new RotateImage(image);
        BufferedImage bufferedImage = rotateImage.rotateImage(5);
        Image image1 = null;
        try{
            ImageIOUtil.writeImage(
                    bufferedImage, String.format("pdf-imageRotate.%s", "png"), 300);
            image1 = new Image(ImageIO.read(new File("pdf-imageRotate.png")));
        } catch (Exception e){
            
        }
        MainManager.getInstance().getMainForm().initRecognize(image1);
        
//        try {
//            ImageIO.write(image.getBufferedImage(), "JPG", new File("rotatedImage.jpg"));
//        } catch (Exception e){}

    }

    private void activateController() {
        MainManager.getInstance().initDocument();
    }


    public void resetCaptions() {
        this.selectFiles.setText("Choose");
        this.adjustmentButton.setText("Adjustment Img");
        this.resetButton.setText("Reset");
    }

    public void setEnabled(boolean isEnable) {
        this.selectFiles.setEnabled(isEnable);
    }
}
