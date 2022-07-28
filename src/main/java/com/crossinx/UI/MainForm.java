package com.crossinx.UI;

import javax.swing.*;

public class MainForm extends JFrame{
    private JTabbedPane tabbedPane;
    private RecognitionPanel recognitionPanel;
    private SelectImageControllerPanel selectImageControllerPanel;

    public MainForm() {
        super();
        this.setLocationByPlatform(true);
        this.recognitionPanel = new RecognitionPanel();
        selectImageControllerPanel = new SelectImageControllerPanel();

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Document", null, this.recognitionPanel,
                "");

        JMenuBar menuBar = new JMenuBar();

        this.setJMenuBar(menuBar);
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addComponent(selectImageControllerPanel)
                        .addComponent(tabbedPane)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                        .addComponent(selectImageControllerPanel)
                )
                .addComponent(tabbedPane)
        );
    }

    public void initRecognize(Image image){
        this.recognitionPanel.setImage(image);
    }

    public void selectTab(int index) {
        this.tabbedPane.setSelectedIndex(index);
    }

}
