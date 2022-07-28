package com.crossinx.UI;

import com.crossinx.ocr.OcrWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 */
public class RecognitionPanel extends JPanel {
    private ImageEditPanel imagePanel;
    private JPanel infoPanel;
    private Image image;
    private JTextField passportIDTextField = new JTextField("");

    private JButton recognizeButton = new JButton("Recognize");

    private int imageIndex = -2;

    /**
     */
    public RecognitionPanel() {
        super();
        this.setBorder(BorderFactory.createBevelBorder(1));
        this.imagePanel = new ImageEditPanel(this);
        this.infoPanel = new JPanel();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(this.imagePanel)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, true)
                                .addComponent(this.recognizeButton)
                                .addComponent(this.infoPanel)
                        )
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(this.imagePanel))
                .addComponent(this.recognizeButton)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(this.infoPanel)
                        )
                )
        );

        this.infoPanel.setLayout(new GridLayout(3, 2));
        this.infoPanel.add(this.passportIDTextField);

        this.recognizeButton.addActionListener(e -> clickRecognize());
        this.recognizeButton.setEnabled(false);

    }

    public void setImage(Image image){
        this.image = image;
        this.recognizeButton.setEnabled(true);
        this.imagePanel.setNewImage(image.getBufferedImage());
    }


    private void clickRecognize() {
        BufferedImage image = this.imagePanel.getImage();
        Rectangle r = this.imagePanel.getSelectedArea();
        try {
            String ocrString = OcrWorker.processOcr(image, r);
            this.passportIDTextField.setText(ocrString);
        } catch (Exception e) {
            this.passportIDTextField.setText("");
        }
    }


}