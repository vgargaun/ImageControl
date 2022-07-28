package com.crossinx.UI.imageViewPort;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;

class RectanglePanel extends JPanel {
    private final JPanel parentPanel;

    public RectanglePanel(JPanel parentPanel, int thickness) {
        super();
        this.parentPanel = parentPanel;

        this.setPreferredSize(new Dimension(100, 100));
        this.setBorder(new BorderUIResource.LineBorderUIResource(Color.RED, thickness));
        this.setOpaque(false);
        Movement mouseMovement = new Movement();
        this.addMouseListener(mouseMovement);
        this.addMouseMotionListener(mouseMovement);

    }

    @Override
    public void setLocation(int x, int y) {
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x + this.getWidth() > parentPanel.getWidth()) {
            x = parentPanel.getWidth() - this.getWidth();
        }
        if (y + this.getHeight() > parentPanel.getHeight()) {
            y = parentPanel.getHeight() - this.getHeight();
        }
        super.setLocation(x, y);
    }


}
