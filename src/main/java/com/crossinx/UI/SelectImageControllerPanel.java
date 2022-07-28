package com.crossinx.UI;


import com.crossinx.MainManager;
import com.crossinx.UI.openCV.OpenCVService;

import javax.swing.*;
import java.awt.*;

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
