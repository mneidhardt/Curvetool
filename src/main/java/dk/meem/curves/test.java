package dk.meem.curves;

class test {

    public static void main(String args[]) {
        Point a = new Point(1.0, 1.0);
        Point b = new Point(1.0, 6.0, true);
        Point c = new Point(3.0, 6.0, true);
        Point d = new Point(6.0, 1.0);

        System.out.println("a=" + a.toString());
        System.out.println("b=" + b.toString());
        System.out.println("a+b=" + a.add(b).toString());
        System.out.println("a-b=" + a.subtract(b).toString());

        Point c2 = a.add(b);
        c2 = c2.divideBy(2);
        System.out.println("C=(a+b)/2: " + c2.toString());

        Point p = new Point(3.0, 10.0);
        Point q = new Point(9.0, 13.0);
        System.out.println("p=" + p.toString() + " q=" + q.toString());
        p = q.subtract(p);
        System.out.println("p=" + p.toString() + " q=" + q.toString());
        // drawBezierCurve(a,b,c,d, 0);
    }

/*
    public static void drawBezierCurve(Point P1, Point P2, Point P3, Point P4, int count) {
    
        if (count == 0) {
            System.out.println("Startpoints: " + P1.toString() + " " + P2.toString() + " " + P3.toString() + " " + P4.toString());
        }
        else if (count == 5) {
            System.out.println("From " + P1.toString() + " to " + P4.toString());
            //g.drawLine((int)punkter[0].x, (int)punkter[0].y, (int)punkter[3].x, (int)punkter[3].y);
        }
        else {
            Point[] tmp = subDivideCurve(P1, P2, P3, P4);
            
            drawBezierCurve(tmp[0], tmp[1], tmp[2], tmp[3], count+1);
            drawBezierCurve(tmp[3], tmp[4], tmp[5], tmp[6], count+1);
        }
    }


    public static Point[] subDivideCurve(Point P1, Point P2, Point P3, Point P4) {
        
        Point L2 = P1.add(P2);
        L2.divideBy(2.0);

        Point H = P2.add(P3);
        H.divideBy(2.0);

        Point L3 = L2.add(H);
        L3.divideBy(2.0);

        Point R3 = P3.add(P4);
        R3.divideBy(2.0);
        
        Point R2 = R3.add(H);
        R2.divideBy(2.0);
        
        Point L4 = L3.add(R2);
        L4.divideBy(2.0);
        
        Point[] result = { P1, L2, L3, L4, R2, R3, P4 };
        return result;
    }
*/        
}
