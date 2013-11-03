import javax.swing.*;
import java.awt.*;
//import java.awt.Graphics;
import java.util.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;


class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {

    // ------ First some global vars ------------
 
    private static final int st_neutral=0;
    private static final int st_selectpoint=1;
    private static final int st_deletepoint=2;
    private static final int st_newstartpoint=3;
    private int STATE=st_neutral;

    private int maxX, maxY;
    private int startX=0, startY=0;
    private int overPoint = -1;
    private boolean wholecurveselected=false;
    private boolean bothCtrlPoints=false;
    private Color backgroundColor = new Color(255,255,255);
    private Color curvecolor = new Color(150,150,150);
    private Color pointcolor = new Color(200,200,200);
    private Color cpointcolor = new Color(180,20,0);
    
    static final int arcwidth=10;			// Width of the circles I draw.
    static final int halfwidth=arcwidth/2;
    static final float PI = 3.14159265358979323846f;
    
    private HCurve curve = new HCurve();
    
    Mainframe creator;
    // ------ End of global vars ----------


    // Constructor:
    DrawPanel(int x, int y, Mainframe cr)	{
        maxX=x; maxY=y;
        setPreferredSize(new Dimension(maxX, maxY));
        addMouseListener(this);
        addMouseMotionListener(this);

        creator = cr;
    }


    public void clearCurve() {
        curve = new HCurve();
        repaint();
    }

    private void do_interPolation() {
        curve.calc_controlpoints();
    }


    public void interPolate() {
        do_interPolation();
        repaint();
    }

        // This will create a number of curves on the outside of the primary one,
        // like height curves on a map.
    public void expandCurve() {
        curve.expand();
        //interPolate();
    }

    public void paintComponent(Graphics g_orig) {
        Graphics2D g = (Graphics2D)g_orig;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(backgroundColor);
        g.fillRect(0,0, this.getWidth(), this.getHeight());
    
        if (curve.isReady()) {
            ArrayList c = curve.getCurve(0);
            drawCurve(g, c);
            drawPoints(g, c);

            int sz = c.size();
            for (int cid=1; cid<100; cid++) {
                c = curve.getCurve(cid);
                if (c.size() == sz) { drawCurve(g, c); }
            }
        }
    }

    public void drawPoints(Graphics g, ArrayList c) {
        boolean drawcp=false;

        for (int i=0; i<c.size(); i++) {
            Point p = (Point)c.get(i);

            if (p.isControlPoint()) {
                g.setColor(cpointcolor);
                g.drawRect(Math.round((float)p.getX() - halfwidth),
                           Math.round((float)p.getY() - halfwidth), arcwidth, arcwidth);

                if (curve.isReady()) {
                    Point anchor; // Find out which normal point this control point is for.
                    if (((Point)c.get(i-1)).isControlPoint()) { anchor = (Point)c.get(i+1); }
                    else { anchor = (Point)c.get(i-1); }
                    g.drawLine(Math.round((float)anchor.getX()), Math.round((float)anchor.getY()),
                               Math.round((float)p.getX()), Math.round((float)p.getY()));
                }
            }
            else {
                g.setColor(pointcolor);
                g.fillArc(Math.round((float)p.getX() - halfwidth),
                          Math.round((float)p.getY() - halfwidth),
                          arcwidth, arcwidth, 0, 360);
            }
        }

    }

    private void drawCurve(Graphics g, ArrayList c) {
        g.setColor(curvecolor);

        int i=0;
        while (true) {
            drawBezierCurve((Point)c.get(i),
                            (Point)c.get(i+1),
                            (Point)c.get(i+2),
                            (Point)c.get(i+3),
                            0,
                            g);

            if (i == c.size()-4) { break; }
            i+=3;
        }


/*
        if (curve.turnsClockwise()) { System.out.println("cw"); }
        else { System.out.println("Ccw"); }
        Point tangent = ((Point)c.get(4)).subtract((Point)c.get(2));
        Point p = (Point)c.get(3);
        g.drawLine(Math.round((float)p.getX()), Math.round((float)p.getY()),
                   Math.round((float)p.getX()-(float)tangent.getY()), Math.round((float)p.getY()+(float)tangent.getX()));
*/
    }


    public void mousePressed(MouseEvent e) {
        int op = curve.overPoint(e.getX(), e.getY(), arcwidth);


        if (op > -1) {
		    System.out.println("Over point " + op);
            if (curve.isControlPoint(op)) { overPoint = op; }
            else                          { overPoint = op; }
        }
        else {
            if (STATE == st_neutral && e.getButton() == 1) {
                curve.addPoint(new Point((double)e.getX(), (double)e.getY()));
            }
            else if (STATE == st_selectpoint && e.getButton() == 1) {
                ; //selectedPoint = -1;
            }
        }

        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        overPoint = -1;
        wholecurveselected=false;
        bothCtrlPoints=false;
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
        if (wholecurveselected) {
            /*
            float transX = (float)e.getX() - (Float)pointset.getPoint(0, overPoint);
            float transY = (float)e.getY() - (Float)pointset.getPoint(1, overPoint);
            pointset.translate(transX, transY);
            ctrlpointset.translate(transX, transY);
            interPolate();
            */
        }
        else if (overPoint > -1 && STATE == st_neutral) {
            curve.movePoint(overPoint, new Point((double)e.getX(), (double)e.getY()));

			repaint();
        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
        setCursor(creator.getCurrCursor());		// Overkill?
    }



   // Simply returns the coordinates of the current point set as a string.
/*
    public String dumpCoordinates() {
        pointset.flipAroundXAxis();
        ArrayList[] points = pointset.getPoints();

        String coords = String.valueOf(points[0].size()) + "\n";

        for (int i=0; i<points[0].size(); i++) {
            coords += i + " " + String.valueOf(Math.round((Float)points[0].get(i))) + " " +
                      String.valueOf(Math.round((Float)points[1].get(i))) + "\n";
        }

        return coords;
    }

    public String getBezierAsPostScript() {
        String pscode = "";


        if (curve.resultReady()) {
            pscode = "%.5 .5 scale % scale coordinate system(?)\n" +
                     "20 60 translate% put origin here\n" +
                     "% part 2: draw a filled path\n" +
                     "0 0 1 setrgbcolor\n" +
                     ".6 setlinewidth\n";

            pscode += "hoo"; //getBezierData(pointset.getPoints(), ctrlpointset.getPoints());
            pscode += "stroke % draw it.\n" +
                      "% part 3: now we'll put up some text\n" +
                      "%/Helvetica findfont 12 scalefont setfont\n" +
                      "%1 setgray % white\n" +
                      "10 -1 moveto (Cubic Bezier curves in Postscript. Made with Intrpol.) show\n" +
                      "showpage % Marks end of page\n";
        }
        return pscode;
    }

/*
    public String getBezierData(ArrayList curve) {
        // Returns the coords. of points and controlpoints of the entire Bezier-curve

        String coords = "";
        int ci=0;

        for (int i=0; i<points[0].size()-1; i++) {
                // First point P1:
                coords += String.valueOf(Math.round((Float)points[0].get(i))) + " " +
                String.valueOf(Math.round((Float)points[1].get(i))) + " moveto\n";

                // Then Controlpoint 1:
                coords += String.valueOf(Math.round((Float)ctrlpoints[0].get(ci))) + " " +
                String.valueOf(Math.round((Float)ctrlpoints[1].get(ci))) + "\n";
        
                // Then Controlpoint 2:
                coords += String.valueOf(Math.round((Float)ctrlpoints[0].get(ci+1))) + " " +
                String.valueOf(Math.round((Float)ctrlpoints[1].get(ci+1))) + "\n";

                // First point P2:
                coords += String.valueOf(Math.round((Float)points[0].get(i+1))) + " " +
                String.valueOf(Math.round((Float)points[1].get(i+1))) + " curveto\n";
                ci += 2;
        }

        return coords;
}
*/

    public void drawBezierCurve(Point P1, Point P2, Point P3, Point P4, int count, Graphics g) {
    
        if (count >= curve.getMaxdiv()) {
            g.drawLine((int)P1.getX(), (int)P1.getY(), (int)P4.getX(), (int)P4.getY());
        }
        else {
            Point[] tmp = subDivideCurve(P1, P2, P3, P4);
            
            drawBezierCurve(tmp[0], tmp[1], tmp[2], tmp[3], count+1, g);
            drawBezierCurve(tmp[3], tmp[4], tmp[5], tmp[6], count+1, g);
        }
    }


    public Point[] subDivideCurve(Point P1, Point P2, Point P3, Point P4) {
        
        Point L2 = P1.add(P2);
        L2 = L2.divideBy(2.0);

        Point H = P2.add(P3);
        H = H.divideBy(2.0);

        Point L3 = L2.add(H);
        L3 = L3.divideBy(2.0);

        Point R3 = P3.add(P4);
        R3 = R3.divideBy(2.0);
        
        Point R2 = R3.add(H);
        R2 = R2.divideBy(2.0);
        
        Point L4 = L3.add(R2);
        L4 = L4.divideBy(2.0);
        
        Point[] result = { P1, L2, L3, L4, R2, R3, P4 };
        return result;
    }
}
