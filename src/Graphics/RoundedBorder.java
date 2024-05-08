/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author xP
 */
public class RoundedBorder implements Border {
    private int radius;
    private boolean dl;
    private Color color;

    RoundedBorder(int radius, boolean doubleline, Color c) {
        this.radius = radius;
        this.dl=doubleline;
        this.color = c;
    }


    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }


    public boolean isBorderOpaque() {
        return true;
    }


    @Override
    public void paintBorder(Component c, java.awt.Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(11));
        if (!dl) g2.setColor(color);
        g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
        if (dl){
            g2.setStroke(new BasicStroke(8));
            g2.setColor(color);
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}