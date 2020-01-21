import java.awt.*;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

/**
 * class outlining methods of all polygons
 * includes both graphical properties such as stroke width and fill/outline colours,
 * and shape properties such as number of points and coordinates of points
 */
public abstract class Polygon {
    //instance variables setting
    //polygon shape properties
    private double[] xCoords;
    private double[] yCoords;
    private int points;

    //polygon graphics properties
    private Color fillColor;
    private Color outlineColor;
    private int strokeWidth;

    private double initXCoord;
    private double initYCoord;

    /**
     * default constructor when no variables provided
     * x/y coords implemented by mouse handler coordinates
     */
    public Polygon() {
        this.fillColor = Color.WHITE;
        this.outlineColor = Color.BLACK;
        this.strokeWidth = 2;
    }

    /**
     * constructor when specific polygon and coordinates are is known, which is during mouse handler
     *
     * @param xCoords
     * @param yCoords
     * @param points
     */
    public Polygon(double[] xCoords, double[] yCoords, int points) {
        this.xCoords = xCoords;
        this.yCoords = yCoords;
        this.points = points;
    }

    /**
     * apply a translation to list of X/Y coordinates
     *
     * @param mouseXCoord
     * @return
     */
    public void translate(double[] xCoords, double[] yCoords, double mouseYCoord, double mouseXCoord) {
        try {
            //move x/y coordinates by difference between mouse end and start point
            for (int i = 0; i < xCoords.length; i++) {
                xCoords[i] += (mouseXCoord - initXCoord);
                yCoords[i] += (mouseYCoord - initYCoord);
            }
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.WARNING, "Movement Translation Error").showAndWait();
            return;
        }
    }

    //setter methods ***

    /**
     * DrawingApp send info above first left click as initial anchored coordinate
     * polygon object sends back rest of coordinates
     *
     * @param initXCoord
     * @param initYCoord
     */
    public void setInitCoord(double initXCoord, double initYCoord) {
        this.initXCoord = initXCoord;
        this.initYCoord = initYCoord;
    }

    public void setXCoords(double[] xCoords) {
        this.xCoords = xCoords;
    }

    public void setYCoords(double[] yCoords) {
        this.yCoords = yCoords;
    }

    /**
     * draw methods for x-coordinates specific to polygon, overridden by subclass
     *
     * @param xCoords
     */
    public void drawXCoords(double xCoords) {
    }

    /**
     * draw methods for y-coordinates specific to polygon, overridden by subclass
     *
     * @param yCoords
     */
    public void drawYCoords(double yCoords) {
    }

    /**
     * sets color of space inside polygon
     *
     * @param fillColor
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * sets color of line that borders around the polygon
     *
     * @param outlineColor
     */
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    /**
     * sets size of line that borders around the polygon
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    //getter methods ***

    /**
     * getter method for x coords of polygon
     *
     * @return xcoords of polygon
     */
    public double[] getXCoords() {
        return this.xCoords;
    }

    /**
     * getter method for y coords of polygon
     *
     * @return ycoords of polygon
     */
    public double[] getYCoords() {
        return this.yCoords;
    }

    /**
     * getter method for initial x coordinate of left mouse click
     *
     * @return double x coord
     */
    public double getInitXCoord() {
        return this.initXCoord;
    }

    /**
     * getter method for initial y coordinate of left mouse click
     *
     * @return double y coord
     */
    public double getInitYCoord() {
        return this.initYCoord;
    }

    /**
     * getter method for number of points of polygon
     *
     * @return number of points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * getter method for color of inside of polygon
     *
     * @return fillColor
     */
    public Color getFillColor() {
        return this.fillColor;
    }

    /**
     * getter method for color of line bordering polygon
     *
     * @return outlineColor
     */
    public Color getOutlineColor() {
        return this.outlineColor;
    }

    /**
     * getter method for width of line bordering polygon
     *
     * @return strokeWidth
     */
    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public boolean checkContains(double mouseXCoord, double mouseYCoord) {
        return false;
    }
}


