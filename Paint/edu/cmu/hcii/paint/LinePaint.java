package edu.cmu.hcii.paint;

import java.awt.*;

public class LinePaint extends PaintObject {

    private Point start;
    private Point end;

    public double getStartX() { return start.getX(); }
    public double getStartY() { return start.getY(); }
    public double getEndX() { return end.getX(); }
    public double getEndY() { return end.getY(); }

    public void define(Point[] points) {

        if(points == null || points.length == 0) return;

        start = points[0];
        end = points[points.length - 1];

    }

    public Rectangle getBoundingBox() {

        int x1 = (int)start.getX();
        int y1 = (int)start.getY();
        int x2 = (int)end.getX();
        int y2 = (int)end.getY();

        int half = thickness / 2;

        int minX = Math.min(x1, x2) - half;
        int minY = Math.min(y1, y2) - half;
        int maxX = Math.max(x1, x2) + half;
        int maxY = Math.max(y1, y2) + half;

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);

    }

    public void paint(Graphics2D g) {

        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness));
        g.setColor(color);

        g.drawLine((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY());

        g.setStroke(oldStroke);

    }

}
