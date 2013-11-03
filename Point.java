public class Point {
    private double x=0.0;
    private double y=0.0;
    private double z=0.0;
    private boolean ControlPoint=false; 


    Point () {
    }

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Point(double x, double y, boolean isControlPoint) {
        this.x = x;
        this.y = y;
        if (isControlPoint) {
            this.ControlPoint = true;
        }
    }

    public Point add(Point B) {
        return new Point(this.getX()+B.getX(), this.getY()+B.getY());
    }

    public Point subtract(Point B) {
        return new Point(this.getX()-B.getX(), this.getY()-B.getY());
    }

    public Point divideBy(double d) {
        return new Point(this.x/d, this.y/d);
    }

    public Point multBy(double d) {
        return new Point(this.x * d, this.y * d);
    }

    public double distanceTo(Point B) {
        double sum = Math.pow(this.x - B.getX(), 2) +
                     Math.pow(this.y - B.getY(), 2);

        return Math.sqrt(sum);
    }

    public double vectorlength() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public String toString() {
        String msg = "NP: ";

        if (this.isControlPoint()) { msg = "CP: "; }
        return msg + "(" + this.x + ", " + this.y + ")";
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    
    public void setX(double val) { this.x = val; }
    public void setY(double val) { this.y = val; }
    public void setZ(double val) { this.z = val; }

    public boolean isControlPoint() { return ControlPoint; }
    public void setCPvalue(boolean value) { this.ControlPoint = value; }

}
