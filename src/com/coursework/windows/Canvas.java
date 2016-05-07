package com.coursework.windows;

import java.awt.*;
import javax.swing.*;

import com.coursework.figures.Figure;
import com.coursework.main.Main;

public class Canvas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5431118052073932479L;

	public Canvas() {
		super();
		setOpaque(true);
		setBackground(Color.white);
	}
	
	Iterable<Figure> figures;
	public void paintFigures(Iterable<Figure> figures) {
		this.figures = figures;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D graphics = (Graphics2D)g;
		//TODO add it
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/*
		
		Area a = new Area(new Arc2D.Double(100, 100, 200, 200, 90, 10, Arc2D.PIE));
		//Area a = new Area(new Rectangle(100, 100, 100, 100));
		//a.add(new Area(new Rectangle2D.Double(200, 200, 100, 100)));
		//a.add(new Area(new Ellipse2D.Double(100, 100, 200, 200)));
		//a.add(new Area);
		graphics.draw(a);
		
        /*
		if (figures != null) {
			for (Figure f : figures) {
				f.selfPaint(graphics);
			}
		}
		*/
	
		Main.drawFigures(graphics);
	}
	
	/// OLD LOGIC
	/*
	public Canvas() {
		initListeners();
		setBackground(Color.white);
	}

	int size = 40;*/
	/*
	private void initListeners() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked (MouseEvent e) {
				if (canPlace()) {
					shapes.add(new Rectangle(e.getX() -size, e.getY() -size, size*2, size*2));
				}
				repaint();
	          	 // System.out.println("MD");
			}
			/*
			public void mousePressed(MouseEvent e) {
				drawStart = new Point(e.getX(), e.getY());
				drawEnd = drawStart;
				//repaint();    
			}
          
          public void mouseDragged(MouseEvent e) {
        	  drawEnd = new Point(e.getX(), e.getY());
          	  System.out.println("MD");
              repaint();
          }
          
          public void mouseReleased(MouseEvent e) {
			//action at
			//drawStart.x, drawStart.y, e.getX(), e.getY()
        	  drawEnd = new Point(e.getX(), e.getY());
          	  System.out.println("MU");
          	  repaint();
          } *
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {}

			public void mouseMoved(MouseEvent e) {
				tempShape = new Rectangle(e.getX() -size, e.getY() -size, size*2, size*2);
	        	  //drawEnd = new Point(e.getX(), e.getY());
	          	  //System.out.println("MM");
	          	  repaint();
			}
			
		});
		this.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				size += e.getUnitsToScroll();
				tempShape = new Rectangle(e.getX() -size, e.getY() -size, size*2, size*2);
				repaint();	
			}
		});
		
		this.addMouseListener(new MouseListener());
		
	}*/
    /*
    Point drawStart, drawEnd, location;
	private Graphics2D graphSettings;
	Shape tempShape;
    
	List<Shape> shapes = new ArrayList<Shape>();
	
	boolean canPlace () {
		boolean col = false;
		for (Shape s : shapes) {
			Area areaA = new Area(s);
			   areaA.intersect(new Area(tempShape));
			   col = col || !areaA.isEmpty();
		}
		return !col;
	}*/
	///OLD LOGIC END
	
	/*
	@Override
    public void paint(Graphics g) {

		location = this.getLocation();

		//System.out.println("PAINT");
        
        graphSettings = (Graphics2D)g;
        graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphSettings.setPaint(Color.white); //red?
//        graphSettings.fillRect(0, 0, this.getSize().width, this.getSize().height);
        //graphSettings.draw(drawRectangle(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y));
		
/*
      	  System.out.println("NP");
      	  System.out.println(drawStart);
      	  System.out.println(drawEnd);*
		
			//graphSettings.drawArc(drawEnd.x - location.x, drawEnd.y - location.y, 100, 100, 0, 90);
			graphSettings.setPaint(Color.black);
			//graphSettings.drawRect(drawEnd.x, drawEnd.y, 100, 100);
			for (Shape s : shapes) {
				//System.out.println("DRAW");
				graphSettings.draw(s);
			}
			
			//===========   COLLISION   ================
				
			
			if (	canPlace()) {
				graphSettings.setPaint(Color.blue);
			} else {

				graphSettings.setPaint(Color.red);
			}
			if (tempShape != null) {
				graphSettings.draw(tempShape);
			
			}
			//=========== END COLLISION ================
    }
*/
 /*   
    private Rectangle2D.Float drawRectangle(
            int x1, int y1, int x2, int y2)
    {
    	// Get the top left hand corner for the shape
    	// Math.min returns the points closest to 0
    	
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            
            // Gets the difference between the coordinates and 
            
            int width = Math.abs(x1 - x2);
            int height = Math.abs(y1 - y2);

            return new Rectangle2D.Float(
                    x, y, width, height);
    }
    */
    /*



	public void paint(Graphics g) {	
        graphSettings = (Graphics2D)g;
        // Antialiasing cleans up the jagged lines and defines rendering rules
       
        graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // Defines the line width of the stroke
        graphSettings.setStroke(new BasicStroke(4));

graphSettings.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, transparency from 0 tp 1);
			graphSettings.setPaint(Color);
        	
        	graphSettings.draw(Shape);
 */
}
