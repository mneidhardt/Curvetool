import java.util.*;
/*
import java.awt.*;
import javax.swing.*;
*/

public class HCurve {
    private int maxdiv = 9;
    private int norm=0;
    private int numofcurves=0;
    
    private ArrayList[] curves = new ArrayList[100];

    HCurve() {
        for (int i=0; i<curves.length; i++) {
            curves[i] = new ArrayList<Point>();
        }
    }

    public void addPoint(Point p) {
        addPoint(p, 0);
    }

    public void addPoint(Point p, int cid) {
        curves[cid].trimToSize();
        if (curves[cid].size() == 0) {
            curves[cid].add(p);
        }
        else {
            curves[cid].add(new Point(0.0, 0.0, true));
            curves[cid].add(new Point(0.0, 0.0, true));
            curves[cid].add(p);
        }

        if (isReady()) { calc_controlpoints(); }
    }

    public Point setPoint(int pid, Point p) {
	    // Set point in base curve at index cid to p.
		// Returns the old point.
	    Point oldpoint = (Point)curves[0].get(pid);
        curves[0].set(pid, p);
		return oldpoint;
    }

    public void movePoint(int pid, Point newposition) {
	    Point oldpoint = (Point)curves[0].get(pid);
	    boolean cp = oldpoint.isControlPoint();
		newposition.setCPvalue(cp);
		curves[0].set(pid, newposition);

		if (cp) {
		    return;    // Nothing else to do if user moved a control point.
		}

        /* If user moved a normal point, we also need to
		   move the surrounding control points.
		   Otherwise it does not look good. */

		if (pid == 0) {
		    translateControlpoint(pid+1, newposition.subtract(oldpoint));
	    }
		else if (pid == curves[0].size()-1) {
		    translateControlpoint(pid-1, newposition.subtract(oldpoint));
		}
		else {
		    translateControlpoint(pid-1, newposition.subtract(oldpoint));
		    translateControlpoint(pid+1, newposition.subtract(oldpoint));
		}
    }

    public void translateControlpoint(int pid, Point translation) {
        Point currentcp = (Point)curves[0].get(pid);
        Point newcp = currentcp.add(translation);
        newcp.setCPvalue(true);
        curves[0].set(pid, newcp);
	}

    public int getMaxdiv() { return maxdiv; }
    public void setMaxdiv(int value) { maxdiv = value; }

    public boolean isReady() {
        curves[0].trimToSize();
        if (curves[0].size() > 6) { return true; }
        else { return false; }
    }

    public ArrayList getCurve(int cid) {
        if (cid < curves.length) {
            curves[cid].trimToSize();
            return curves[cid];
        }
        else {
            return new ArrayList<Point>();
        }
    }
  

    public void printCurve(int cid) {
        System.out.println("----cid=" + cid);
        for (int j=0; j<curves[cid].size(); j++) {
            System.out.println("  " + curves[cid].get(j).toString() + " ");
        }
        System.out.println("---------");
    }

    public void printArrayList(ArrayList list) {
        for (int i=0; i<list.size(); i++) {
            System.out.print(list.get(i).toString() + " ");
        }
        System.out.println("---------");
    }

    public void calc_controlpoints() {
       calc_controlpoints(0);
    }

    private void calc_controlpoints(int cid) {
        curves[cid].trimToSize();

        ArrayList tvalues = this.euclidean_t_values();

        // Calculate first control point:
        Point A = (Point)curves[cid].get(0);
        Point B = (Point)curves[cid].get(3);
        Point C = (Point)curves[cid].get(6);

        Point divdiff0 = B.subtract(A);
        divdiff0 = divdiff0.divideBy((Double)tvalues.get(1)-(Double)tvalues.get(0));
        Point slope0 = B.subtract(A);
        slope0 = slope0.divideBy(2.0);
        slope0 = slope0.divideBy((Double)tvalues.get(2)-(Double)tvalues.get(1));
        divdiff0 = divdiff0.multBy(3.0);
        slope0 = divdiff0.subtract(slope0);
        slope0 = slope0.divideBy(2.0);
        slope0 = slope0.multBy(((Double)tvalues.get(1)-(Double)tvalues.get(0))/3.0);
        Point firstCP = ((Point)curves[cid].get(0)).add(slope0);
        firstCP.setCPvalue(true);
        curves[cid].set(1, firstCP);

        // Calculate last control point:
        int lastCidx = curves[cid].size()-1;
        Point Z = (Point)curves[cid].get(lastCidx);
        Point Y = (Point)curves[cid].get(lastCidx-3);
        Point X = (Point)curves[cid].get(lastCidx-6);
        int lastTidx = tvalues.size()-1;
        Point divdiffN = Z.subtract(Y);
        divdiffN = divdiffN.divideBy((Double)tvalues.get(lastTidx)-(Double)tvalues.get(lastTidx-1));
        Point slopeN = Z.subtract(X);
        slopeN = slopeN.divideBy(2.0);
        slopeN = slopeN.divideBy((Double)tvalues.get(lastTidx-2)-(Double)tvalues.get(lastTidx-1));
        divdiffN = divdiffN.multBy(3.0);
        slopeN = divdiffN.subtract(slopeN);
        slopeN = slopeN.divideBy(2.0);
        slopeN = slopeN.multBy(((Double)tvalues.get(lastTidx)-(Double)tvalues.get(lastTidx-1))/3.0);
        Point lastCP = ((Point)curves[cid].get(lastCidx)).subtract(slopeN);
        lastCP.setCPvalue(true);
        curves[cid].set(lastCidx-1, lastCP);


        // Now do both control points for each normal point in between:
        //for (int i=1; i<curves[cid].size()-1; i++) {
        int i=3, tvalidx=1;
        while (true) {
            Point P = (Point)curves[cid].get(i+3);
            Point Q = (Point)curves[cid].get(i-3);
            Point slopeOUT = P.subtract(Q);
            Point slopeIN = P.subtract(Q);
            slopeOUT = slopeOUT.divideBy(2.0);
            slopeIN = slopeIN.divideBy(2.0);

            double tdiff1 = (Double)tvalues.get(tvalidx+1) - (Double)tvalues.get(tvalidx);
            slopeOUT = slopeOUT.divideBy(tdiff1);
            slopeIN = slopeIN.divideBy(tdiff1);

            double tdiff2 = (Double)tvalues.get(tvalidx) - (Double)tvalues.get(tvalidx-1);
            slopeOUT = slopeOUT.multBy(tdiff2/3.0);
            slopeIN = slopeIN.multBy(tdiff2/3.0);

            Point preCP = ((Point)curves[cid].get(i)).subtract(slopeOUT);
            Point postCP = ((Point)curves[cid].get(i)).add(slopeIN);

            preCP.setCPvalue(true);
            postCP.setCPvalue(true);

            curves[cid].set(i-1, preCP);
            curves[cid].set(i+1, postCP);

            if (i == curves[cid].size()-4) { break; }
            i += 3;
            ++tvalidx;
        }
    }



    public ArrayList euclidean_t_values() {
        // Calculate the t values from the points we were given.
        // Using the Euclidean norm.
    
        ArrayList tvalues = new ArrayList<Double>();
        tvalues.add(0.0);
        double totaldistance = 0.0;
        curves[0].trimToSize();
 
        int i=0;
        while (true) {
            i += 3;
            if (i < curves[0].size()) {
                Point A = (Point)curves[0].get(i);
                Point B = (Point)curves[0].get(i-3);
                totaldistance += A.distanceTo(B);
                tvalues.add(totaldistance);
            }
            else { break; }
        }

        tvalues.trimToSize();
        return tvalues;
    }

    public boolean isControlPoint(int idx) { return ((Point)curves[0].get(idx)).isControlPoint(); }

    public int overPoint(int x, int y, int arcwidth) {
    // Tells whether the position (x,y) is over one of the existing points.
    // Returns -1 if no points exist or if (x,y) not over one.
    // Returns the index of the point, if (x,y) is over one.
  
        int halfwidth = arcwidth/2;
  
        if (curves[0].size() == 0) {
            return -1;
        }
        else {
            for (int i=0; i<curves[0].size(); i++) {
                Point p = (Point)curves[0].get(i);
                if (Math.abs(x - p.getX()) < halfwidth &&
                    Math.abs(y - p.getY()) < halfwidth) {
                    return i;
                }
            }
        }
  
        return -1;
    }

    public boolean turnsClockwise() {
        int cid = 0;
        int sz = curves[cid].size();

        if (sz < 7) { return false; }
        int positive = 0;
        int negative = 0;

        int i=0;
        while (true) {
            if (sz-i > 6) {
                Point A = ((Point)curves[cid].get(i+6)).subtract((Point)curves[cid].get(i));
                Point B = ((Point)curves[cid].get(i+3)).subtract((Point)curves[cid].get(i));
                if (A.getX()*B.getY() - B.getX()*A.getY() < 0) { ++negative; }
                else { ++positive; }
                i+=3;
            }
            else { break; }
        }

        if (negative > positive) { return true; }
        else { return false; }
    }


    public void expand() {
        boolean cwise = turnsClockwise();
        ++numofcurves;

        for (int cid=0; cid<curves.length; cid++) {
            Point A = (Point)curves[cid].get(0);
            Point B = (Point)curves[cid].get(1);
            addPoint(getNewpoint(A, B, A), cid+1);
    
            int i = 3;
            while (true) {
                if (i < curves[cid].size()-3) {
                    A = (Point)curves[cid].get(i-1);
                    B = (Point)curves[cid].get(i+1);
                    Point C = (Point)curves[cid].get(i);
                    addPoint(getNewpoint(A, B, C), cid+1);
                    i += 3;
                }
                else { break; }
            }
    
            A = (Point)curves[cid].get(curves[cid].size()-2);
            B = (Point)curves[cid].get(curves[cid].size()-1);
            addPoint(getNewpoint(A, B, B), cid+1);
    
            calc_controlpoints(cid+1);
            ++numofcurves;
        }
    }

    /* This creates a new point as follows:
       Compute the vector between points A & B.
       Compute the normal to this vector, normalise it to unit length
       and add it to point C.
       All in all, it creates a new point a bit from point C, along the
       vector as described above.
    */
    public Point getNewpoint(Point A, Point B, Point C) {
        Point tangent = A.subtract(B);
        Point normal = new Point(-tangent.getY(), tangent.getX());
        normal = normal.divideBy(normal.vectorlength());
        normal = normal.multBy(5.0);

        return new Point(C.getX()+normal.getX(), C.getY()+normal.getY()); 
    }
}
