package com.crossinx.UI.imageViewPort;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Movement implements MouseListener, MouseMotionListener {

    private int xLastPos;
    private int yLastPos;

    @Override
    public void mouseClicked(MouseEvent evt) {
        //Do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xLastPos = e.getX();
        yLastPos = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //Do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //Do nothing
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        if (evt.getComponent().getCursor().getType() == Cursor.HAND_CURSOR) {//MOVE
            int xFinalPos = evt.getX() + evt.getComponent().getX() - xLastPos;
            int yFinalPos = evt.getY() + evt.getComponent().getY() - yLastPos;
            evt.getComponent().setLocation(xFinalPos, yFinalPos);
        } else {//RESIZE
            Component c = evt.getComponent();


            int xShift = (evt.getPoint().x - xLastPos);
            int yShift = (evt.getPoint().y - yLastPos);

            if (c.getCursor().getType() == Cursor.E_RESIZE_CURSOR) {
                c.setSize(c.getWidth() + xShift, c.getHeight());
            } else if (c.getCursor().getType() == Cursor.SE_RESIZE_CURSOR) {
                c.setSize(c.getWidth() + xShift, c.getHeight() + yShift);
            } else if (c.getCursor().getType() == Cursor.S_RESIZE_CURSOR) {
                c.setSize(c.getWidth(), c.getHeight() + yShift);
            } else if (c.getCursor().getType() == Cursor.SW_RESIZE_CURSOR) {
                c.setBounds(c.getX() + evt.getPoint().x, c.getY(), c.getWidth() - evt.getPoint().x, c.getHeight() + yShift);
            } else if (c.getCursor().getType() == Cursor.W_RESIZE_CURSOR) {
                c.setBounds(c.getX() + evt.getPoint().x, c.getY(), c.getWidth() - evt.getPoint().x, c.getHeight());
            } else if (c.getCursor().getType() == Cursor.NW_RESIZE_CURSOR) {
                c.setBounds(c.getX() + evt.getX(), c.getY() + evt.getY(), c.getWidth() - evt.getPoint().x, c.getHeight() - evt.getY());
            } else if (c.getCursor().getType() == Cursor.N_RESIZE_CURSOR) {
                c.setBounds(c.getX(), c.getY() + evt.getY(), c.getWidth(), c.getHeight() - evt.getY());
            } else if (c.getCursor().getType() == Cursor.NE_RESIZE_CURSOR) {
                c.setBounds(c.getX(), c.getY() + evt.getY(), c.getWidth() + xShift, c.getHeight() - evt.getY());
            }

            xLastPos = evt.getX();
            yLastPos = evt.getY();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component c = e.getComponent();
        int widthborder = 10;
        if (chekInArea(new Rectangle(widthborder, 0, c.getWidth() - 2 * widthborder, widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        } else if (chekInArea(new Rectangle(0, 0, widthborder, widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
        } else if (chekInArea(new Rectangle(0, widthborder, widthborder, c.getHeight() - 2 * widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        } else if (chekInArea(new Rectangle(0, c.getHeight() - widthborder, widthborder, widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
        } else if (chekInArea(new Rectangle(10, c.getHeight() - widthborder, c.getWidth() - 2 * widthborder, widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        } else if (chekInArea(new Rectangle(c.getWidth() - widthborder, c.getHeight() - widthborder, widthborder, widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
        } else if (chekInArea(new Rectangle(c.getWidth() - widthborder, widthborder, widthborder, c.getHeight() - 2 * widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        } else if (chekInArea(new Rectangle(c.getWidth() - widthborder, 0, widthborder, widthborder), e.getPoint())) {
            c.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
        } else {
            c.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private boolean chekInArea(Rectangle rect, Point point) {
        if (point.x >= rect.x && point.x <= rect.width + rect.x &&
                point.y >= rect.y && point.y <= rect.height + rect.y)
            return true;
        return false;
    }
}
