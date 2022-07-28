package com.crossinx.UI;

import com.crossinx.UI.imageViewPort.ViewPort;

import javax.swing.*;
import java.awt.Image;
import java.awt.*;
import java.awt.image.BufferedImage;

class ImageEditPanel extends JPanel {

    private ViewPort viewPort;

    ImageEditPanel(RecognitionPanel recognitionPanel) {
        JScrollPane imageScrollPane;
        {
            this.viewPort = new ViewPort();
            imageScrollPane = new JScrollPane(this.viewPort);
            viewPort.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            imageScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addComponent(imageScrollPane)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                )
                .addComponent(imageScrollPane)

        );
    }


    public BufferedImage getImage() {
        return this.viewPort.getOriginalImage();
    }

    Rectangle getSelectedArea() {
        return this.viewPort.getSelectedAreaRect();
    }


//    void setSide(SideType side) {
//        this.buttonPanel.setSide(side);
//    }

    void setNewImage(BufferedImage image) {
        this.viewPort.setImage(image);
    }

    void clear() {
        BufferedImage b_img = new BufferedImage(100, 100, Image.SCALE_DEFAULT);
        Graphics2D graphics = b_img.createGraphics();
        graphics.setPaint(new Color(255, 255, 255));
        graphics.fillRect(0, 0, b_img.getWidth(), b_img.getHeight());
        this.viewPort.setImage(b_img);
    }

}
