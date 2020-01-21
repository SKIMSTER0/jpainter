import javafx.scene.paint.Color;

/**
 * Rectangle class contains essential information about rectangle polygon: number of points (4), and its x/y coordinates points
 * contains setter method for coordinates specifically for drawing, and a regular one
 */
public class Rectangle extends Polygon {
    //instance variables
    private final int points = 4;
    private double[] xCoords;
    private double[] yCoords;

    public Rectangle() {
        super();
        //ensures only 3 pairs of x and y coordinates describing each triangle
        this.xCoords = new double[4];
        this.yCoords = new double[4];
    }

    /**
     * constructor for when coordinates known (when placing an already sized rectangle)
     *
     * @param xCoords
     * @param yCoords
     */
    public Rectangle(double[] xCoords, double[] yCoords) {
        super(xCoords, yCoords, 4);
        this.xCoords = xCoords;
        this.yCoords = yCoords;
    }

    /**
     * setter method for x coordinates for constructor
     *
     * @param xCoords
     */
    public void setXCoords(double[] xCoords) {
        this.xCoords = xCoords;
    }

    /**
     * setter method for y coordinates for constructor
     *
     * @param yCoords
     */
    public void setYCoords(double[] yCoords) {
        this.yCoords = yCoords;
    }

    /**
     * setter method for x coordinates
     *
     * @param xCoord xcoord of where mouse is
     * @return array of 4 xcoordinates describing rectangle
     */
    @Override
    public void drawXCoords(double xCoord) {
        this.xCoords[0] = super.getInitXCoord();
        this.xCoords[1] = xCoord;
        this.xCoords[2] = xCoord;
        this.xCoords[3] = super.getInitXCoord();
    }

    /**
     * setter methods for y coordinates
     *
     * @param yCoord ycoord of where mouse is
     * @return array of 4 ycoordinates describing rectangle
     */
    @Override
    public void drawYCoords(double yCoord) {
        this.yCoords[0] = super.getInitYCoord();
        this.yCoords[1] = super.getInitYCoord();
        this.yCoords[2] = yCoord;
        this.yCoords[3] = yCoord;
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
     * getter method for number of points of triangle object
     *
     * @return number of points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * checks whether point lies inside rectangle by checking boundaries of coordinates
     *
     * @param mouseXCoord
     * @param mouseYCoord
     * @return
     */
    @Override
    public boolean checkContains(double mouseXCoord, double mouseYCoord) {
        //checks whether mouse coordinates within x/y bounds of rectangle
        boolean withinXBounds = false;
        boolean withinYBounds = false;

        //if rectangle was made from left to right
        if (xCoords[0] < xCoords[2]) {
            //must be between leftmost and rightmost x coordinates
            if (mouseXCoord > xCoords[0] && mouseXCoord < xCoords[2]) withinXBounds = true;
        } else if (mouseXCoord < xCoords[0] && mouseXCoord > xCoords[2]) withinXBounds = true;

        //if rectangle was made up to down
        if (this.yCoords[0] < yCoords[2]) {
            //must be between topmost and bottommost x coordinates
            if (mouseYCoord > yCoords[0] && mouseYCoord < yCoords[2]) withinYBounds = true;
        } else if (mouseYCoord < yCoords[0] && mouseYCoord > yCoords[2]) withinYBounds = true;

        if (withinXBounds && withinYBounds) return true;
        else return false;
    }
}
