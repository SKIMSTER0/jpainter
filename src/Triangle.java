import javafx.scene.paint.Color;

/**
 * Triangle class contains essential information about triangle polygon: number of points (3), and its x/y coordinates points
 * contains setter method for coordinates specifically for drawing, and a regular one
 */
public class Triangle extends Polygon {
    //instance variables setting
    private final int points = 3;
    private double[] xCoords;
    private double[] yCoords;

    public Triangle() {
        super();
        this.xCoords = new double[3];
        this.yCoords = new double[3];
    }

    /**
     * constructor for when coordinates known (when placing an already sized triangle)
     *
     * @param xCoords
     * @param yCoords
     */
    public Triangle(double[] xCoords, double[] yCoords) {
        super(xCoords, yCoords, 3);
        setXCoords(xCoords);
        setYCoords(yCoords);
    }

    /**
     * getter method for x coordinates of all points
     *
     * @return x coords
     */
    @Override
    public double[] getXCoords() {
        return this.xCoords;
    }

    /**
     * getter method for y coordinates of all points
     *
     * @return y coords
     */
    @Override
    public double[] getYCoords() {
        return this.yCoords;
    }

    /**
     * setter method for y coordinates specifically for drawing
     *
     * @param xCoord xcoord of where mouse is
     * @return array of 4 xcoordinates describing rectangle
     */
    @Override
    public void drawXCoords(double xCoord) {
        this.xCoords[0] = super.getInitXCoord();
        this.xCoords[1] = xCoord;
        //ensures second x point is between x points of 1st and second, and reflected above/below initial x (using abs)
        this.xCoords[2] = (Math.abs(xCoord - super.getInitXCoord()) / 2);
        //ensures second x point can work when reflected left/right initial x
        if (super.getInitXCoord() > xCoord) {
            this.xCoords[2] += xCoord;
        } else {
            this.xCoords[2] += super.getInitXCoord();
        }
    }

    /**
     * setter method for x coordinates
     *
     * @param yCoord ycoord of where mouse is
     * @return array of 4 ycoordinates describing rectangle
     */
    @Override
    public void drawYCoords(double yCoord) {
        this.yCoords[0] = super.getInitYCoord();
        this.yCoords[1] = super.getInitYCoord();
        this.yCoords[2] = yCoord;
    }

    /**
     * getter method for number of points of triangle object
     *
     * @return number of points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * checks whether points lies inside triangle using only coordinates with bayocentric equations
     *
     * @param mouseXCoord
     * @param mouseYCoord
     * @return
     */
    @Override
    public boolean checkContains(double mouseXCoord, double mouseYCoord) {
        //bayocentric equation check
        double area = 0.5 * (-yCoords[1] * xCoords[2] + yCoords[0] * (-xCoords[1] + xCoords[2]) + xCoords[0] * (yCoords[1] - yCoords[2]) + xCoords[1] * yCoords[2]);
        double s = 1 / (2 * area) * (yCoords[0] * xCoords[2] - xCoords[0] * yCoords[2] + (yCoords[2] - yCoords[0]) * mouseXCoord + (xCoords[0] - xCoords[2]) * mouseYCoord);
        double t = 1 / (2 * area) * (xCoords[0] * yCoords[1] - yCoords[0] * xCoords[1] + (yCoords[0] - yCoords[1]) * mouseXCoord + (xCoords[1] - xCoords[0]) * mouseYCoord);

        if (s > 0 && t > 0 && 1 - s - t > 0) return true;
        else return false;
    }
}
