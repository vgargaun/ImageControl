package com.crossinx.UI.imageViewPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ViewPort extends JPanel{
    private static final float MAX_SCALE = 5.0f;
    private static final float MIN_SCALE = 0.1f;
    private static final float STEP_SCALE = 0.1f;

    private BufferedImage originalImage;
    private RectanglePanel areaPanel;
    private float scaleKoef = 1.0f;
    private Point startPoint = new Point();
    private Point endPoint = new Point();
    private int borderAreaSize = 2;
    public ViewPort() {
        this.setBackground(Color.YELLOW);
        this.areaPanel = new RectanglePanel(this, this.borderAreaSize);
        this.setLayout(null);

        this.add(this.areaPanel);
        areaPanel.setPreferredSize(new Dimension(100, 100));
        areaPanel.setBounds(0, 0, 100, 100);
        initMouseListerners();
        initResizeListerner();
    }

    private void initResizeListerner() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float originalCoef;
                try {
                    originalCoef = originalImage.getWidth() * 1.0f / originalImage.getHeight();
                } catch (NullPointerException ee) {
                    originalCoef = 1;
                }
                float factCoef = e.getComponent().getWidth() * 1.0f / e.getComponent().getHeight();

                if (Math.abs(originalCoef - factCoef) > 0.01) {
                    removeComponentListener(this);
                    // logger.info("ratio is broken");
                    int width = e.getComponent().getWidth(), height = e.getComponent().getHeight();
                    try {
                        if (factCoef > originalCoef) {
                            setScale(width * 1.0f / originalImage.getWidth());
                        } else {
                            setScale(height * 1.0f / originalImage.getHeight());
                        }
                    } catch (NullPointerException ee) {

                    }
                    addComponentListener(this);
                    // logger.info("ratio  restored");
                }
            }
        });
    }

    private void initMouseListerners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                areaPanel.setLocation(e.getPoint());
                areaPanel.setSize(0, 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();
                areaPanel.setSize(e.getPoint().x - startPoint.x, e.getPoint().y - startPoint.y);

            }
        });
    }

    private float setScale(float scale) {
        if (scale <= MAX_SCALE && scale >= MIN_SCALE) {
            this.scaleKoef = scale;
            this.setPreferredSize(new Dimension((int) (this.originalImage.getWidth() * scaleKoef), (int) (this.originalImage.getHeight() * scaleKoef)));
            this.setBounds(0, 0, (int) (this.originalImage.getWidth() * scaleKoef), (int) (this.originalImage.getHeight() * scaleKoef));
        }
        return this.scaleKoef;
    }


    public void setImage(BufferedImage image) {
        this.originalImage = image;
        repaint();
        setScale(1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawImage(this.originalImage, 0, 0, getWidth(), getHeight(), this);

    }

    public Rectangle getSelectedAreaRect() {
        return new Rectangle((int) ((this.areaPanel.getX() + this.borderAreaSize) / this.scaleKoef), (int) ((this.areaPanel.getY() + this.borderAreaSize) / this.scaleKoef), (int) ((this.areaPanel.getWidth() - (this.borderAreaSize * 2)) / this.scaleKoef), (int) ((this.areaPanel.getHeight() - (this.borderAreaSize * 2)) / this.scaleKoef));
    }



    public void upScale() {
        setScale(this.scaleKoef + STEP_SCALE);
    }


    public void downScale() {
        setScale(this.scaleKoef - STEP_SCALE);
    }


    public void leftRotate() {

    }


    public void rightRotatre() {

    }

    public BufferedImage getOriginalImage() {
        return this.originalImage;
    }
}
