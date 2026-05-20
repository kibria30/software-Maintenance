package edu.cmu.hcii.paint;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PaintCanvas extends JPanel {

    Vector history;
    
    Vector paintObjects;

    private PaintObject temporaryObject;
    private PaintObject hoveringObject;
    
    public PaintCanvas(int initialWidth, int initialHeight) {
        
        setPreferredSize(new Dimension(initialWidth, initialHeight));
        
        paintObjects = new Vector();
        
        history = new Vector();
        
    }
    
    public void paintComponent(Graphics g) {
        
		((Graphics2D) g).addRenderingHints(
			new java.awt.RenderingHints(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON));
        
        Rectangle clipBounds = g.getClipBounds();
        g.setColor(Color.white);
        g.fillRect((int)clipBounds.getX(), (int)clipBounds.getY(), 
                    (int)clipBounds.getWidth(), (int)clipBounds.getHeight());
        
        Iterator paintObjectIterator = paintObjects.iterator();
        while(paintObjectIterator.hasNext())
			try {
		        ((PaintObject)paintObjectIterator.next()).paint((Graphics2D)g); 
			} catch(Exception e) { 
				System.err.println("The graphics context isn't a Graphics2D. No anti-aliasing!");
			}
        
        if(temporaryObject != null) temporaryObject.paint((Graphics2D)g);
        
		if(hoveringObject != null) {
			
			Rectangle rect = hoveringObject.getBoundingBox();
			g.setColor(Color.black);
			g.drawRect((int)rect.getX() - 1, (int)rect.getY() - 1, (int)rect.getWidth() + 2, (int)rect.getHeight() + 2);
			hoveringObject.paint((Graphics2D)g);
			
		}
        
    }
    
    public int sizeOfHistory() { return history.size(); }
    
    public void setTemporaryObject(PaintObject temporaryObject) {
        
        this.temporaryObject = temporaryObject;
        ensurePreferredSizeForObject(temporaryObject);
        repaint();
        
    }
    
    public void setHoveringObject(PaintObject hoveringObject) {
    	
    	this.hoveringObject = hoveringObject;
    	repaint();
    	
    }
    
    public void addPaintObject(PaintObject newObject) {
        
        history.addElement(new Vector(paintObjects));
        paintObjects.addElement(newObject);
        ensurePreferredSizeForObject(newObject);
        repaint();
        
    }
    
    public void clear() {
        
        history.addElement(new Vector(paintObjects));
        paintObjects.removeAllElements();
        repaint();

    }

    public void undo() { 

        if(history.size() == 0) return;
        
        paintObjects = (Vector)history.lastElement();
        history.removeElement(history.lastElement());
        repaint();
        
    }

    private void ensurePreferredSizeForObject(PaintObject object) {

        if(object == null) return;

        Rectangle rect = object.getBoundingBox();
        if(rect == null) return;

        Dimension current = getPreferredSize();

        int right = (int)(rect.getX() + rect.getWidth());
        int bottom = (int)(rect.getY() + rect.getHeight());

        int neededWidth = current.width;
        int neededHeight = current.height;

        if(right > neededWidth) neededWidth = right + 2;
        if(bottom > neededHeight) neededHeight = bottom + 2;

        if(neededWidth != current.width || neededHeight != current.height) {
            setPreferredSize(new Dimension(neededWidth, neededHeight));
            revalidate();
        }

    }


}
