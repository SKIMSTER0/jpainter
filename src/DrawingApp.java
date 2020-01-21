
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * drawing application that allows drawing rectangles, triangles
 * with options such as picking color, deleting/moving/resetting shapes
 * also allows changing stroke width for shapes and moving them to front and back
 * uses mouse left and right clicks to capture inputs
 *
 * @author Stephen Kim 000801165
 */
public class DrawingApp extends Application {
    //height and width of app
    private int appHeight;
    private int appWidth;

    //pane for handling GUI placement
    private GridPane root;
    private Scene scene;
    private Canvas canvas;
    //graphics context that handles button logic and handling
    private GraphicsContext gc;

    //colors for selecting fill and outline border of shapes of drawn shape poly
    private int strokeWidth;
    private Color fillColor;
    private Color outlineColor;

    //next polygon to be drawn
    private Polygon poly;
    //polygon that is selected
    private Polygon polySelected;
    //selected polygon outline is turned red, this is that polygon's previous outline that would be overwritten
    private Color polySelectedOutline;
    //arraylist that contains expandable list of polygon objects drawn on canvas
    private ArrayList<Polygon> shapeList;

    //button options to select whether user wants to draw triangle or rectangle
    private Button triangleSelect;
    private Button rectangleSelect;

    //button options to select user defined colors with ColorPicker
    private ColorPicker fillColorPicker;
    private ColorPicker outlineColorPicker;

    private Label strokeWidthLabel;
    private TextField strokeWidthInput;

    //button options to clear various things about shape arraylist
    private Button clear;
    private Button resetColor;

    //button options for selected shape
    private Button deleteShape;
    private Button toFront;
    private Button toBack;

    //instructions for how to use app
    private Label instructions;
    private final String instructionsText = "INSTRUCTIONS\n" +
            "Left Mouse click and drag to draw a new object\n" +
            "Right Mouse click to select object\n" +
            "Right Mouse click and drag to move selected object\n" +
            "Delete/Move to Front-Back using Buttons\n";

    /**
     * components and event handlers
     *
     * @param stage The main stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        //initialize polygon shape arraylist
        shapeList = new ArrayList<Polygon>();
        //set default shape to be triangle
        poly = new Triangle();

        //height and width of drawing application
        appWidth = 1200;
        appHeight = 700;
        //pane and scene for windows
        root = new GridPane();
        scene = new Scene(root, appWidth, appHeight);

        //sets parameters for gridpane
        //how tall canvas height should be
        RowConstraints canvasHeight = new RowConstraints(400);
        root.getRowConstraints().addAll(canvasHeight);
        //space between each grid element
        root.setVgap(50);
        root.setHgap(10);

        //canvas for graphicscontext, enabling mouse handlers
        canvas = new Canvas(1000, 400);
        gc = canvas.getGraphicsContext2D();

        //settings title and scene
        stage.setTitle("DrawingApp"); // set the window title here
        stage.setScene(scene);

        //making new instances of GUI components ***
        //button options to select whether user wants to draw triangle or rectangle
        triangleSelect = new Button("Triangle");
        rectangleSelect = new Button("Rectangle");

        //button options to select user defined colors with ColorPicker
        strokeWidthLabel = new Label("Stroke Size:");
        strokeWidthInput = new TextField();
        strokeWidthInput.setPromptText("Set Stroke Size");
        //sets default colours
        fillColor = Color.WHITE;
        outlineColor = Color.BLACK;
        strokeWidth = 2;
        //set new ColorPicker class and set it as button
        fillColorPicker = new ColorPicker();
        fillColorPicker.getStyleClass().add("button");
        outlineColorPicker = new ColorPicker();
        outlineColorPicker.getStyleClass().add("button");

        //button options to clear various things about shape arraylist
        clear = new Button("Clear App");
        resetColor = new Button("Reset Shape Colors");

        //button options for selected shape
        deleteShape = new Button("Delete");
        toFront = new Button("To Front");
        toBack = new Button("To Back");

        //instructions for how to play app
        instructions = new Label(this.instructionsText);

        //boxes that describe positions of buttons/textfields in order
        HBox shapeOptions = new HBox(10);
        HBox propertiesOptions = new HBox(10);
        HBox generalOptions = new HBox(10);
        HBox selectedOptions = new HBox(10);
        HBox instructionOptions = new HBox(10);

        //add GUI components to panes/root***
        //HBox is pane object that groups together similar options horizontally
        shapeOptions.getChildren().addAll(triangleSelect, rectangleSelect);
        propertiesOptions.getChildren().addAll(strokeWidthLabel, strokeWidthInput, fillColorPicker, outlineColorPicker);
        generalOptions.getChildren().addAll(clear, resetColor);
        selectedOptions.getChildren().addAll(deleteShape, toFront, toBack);
        instructionOptions.getChildren().addAll(instructions);

        //where HBoxes go in the gridpane
        root.add(canvas, 0, 0, 3, 1);
        root.add(shapeOptions, 0, 1, 1, 1);
        root.add(propertiesOptions, 0, 2, 1, 1);
        root.add(generalOptions, 1, 1, 1, 1);
        root.add(selectedOptions, 1, 2, 1, 1);
        root.add(instructionOptions, 2, 1, 1, 2);

        //adding listeners ***
        //listeners for buttons
        rectangleSelect.setOnAction(this::rectangleHandler);
        triangleSelect.setOnAction(this::triangleHandler);
        clear.setOnAction(this::clearHandler);
        resetColor.setOnAction(this::resetColorHandler);
        deleteShape.setOnAction(this::deleteShapeHandler);
        toFront.setOnAction(this::toFrontHandler);
        toBack.setOnAction(this::toBackHandler);

        //listeners for mouse events
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::pressHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clickHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::dragHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::releaseHandler);

        //listeners for colorpickers
        fillColorPicker.setOnAction((EventHandler) t -> {
            //colorpicker thread as parameter to setfillcolor
            fillColor = fillColorPicker.getValue();
        });
        outlineColorPicker.setOnAction((EventHandler) t -> {
            //colorpicker thread as parameter to setoutlinecolor
            outlineColor = outlineColorPicker.getValue();
        });

        //show stage ***
        stage.show();
    }

    /**
     * takes input from textfield for stroke and make that width of stroke
     */
    private void setStrokeWidth() {
        //handling error if strokewidth textbox has no characters (nullpointer) or non-integers
        try {
            strokeWidth = Integer.parseInt(strokeWidthInput.getText());
        } catch (NullPointerException | NumberFormatException e) {
            //dialog pop up for no/non-integer input in strokewidth textfield
            new Alert(Alert.AlertType.WARNING, "Invalid Line Width").showAndWait();
        }
    }

    /**
     * draws global next polygon object to be drawn and refreshes shapelist by drawing all polygons in shapelist
     */
    private void drawAll() {
        //refreshes background of all drawn polygons in shapeList
        clearCanvas();

        try {
            //draw all polygons through shapelist using their properties
            for (int i = 0; i < shapeList.size(); i++) {
                double[] xCoords = shapeList.get(i).getXCoords();
                double[] yCoords = shapeList.get(i).getYCoords();
                int points = shapeList.get(i).getPoints();

                gc.setFill(shapeList.get(i).getFillColor());
                gc.setStroke(shapeList.get(i).getOutlineColor());
                gc.setLineWidth(shapeList.get(i).getStrokeWidth());

                gc.fillPolygon(xCoords, yCoords, points);
                gc.strokePolygon(xCoords, yCoords, points);
            }
        } catch (NullPointerException e) {
            //catching nullpointer for if shapeList has size 0
            new Alert(Alert.AlertType.WARNING, "No shape to draw").showAndWait();
            return;
        }
    }

    /**
     * draws the polygon using an anchor initial point
     *
     * @param poly
     */
    private void draw(Polygon poly) {
        //ensures fill/stroke does not leave afterimage behind, constantly refreshes background shapelist
        drawAll();
        //ensures properties appear as soon as drag
        initPoly();

        //polygon properties
        double[] xCoords = poly.getXCoords();
        double[] yCoords = poly.getYCoords();
        int points = poly.getPoints();
        //draws polygon on to canvas
        gc.fillPolygon(xCoords, yCoords, points);
        gc.strokePolygon(xCoords, yCoords, points);
    }

    //event handler listeners ***
    //handlers for buttons

    /**
     * initializes next polygon object to be drawn as a triangle
     *
     * @param actionEvent
     */
    private void triangleHandler(ActionEvent actionEvent) {
        poly = new Triangle();
    }

    /**
     * initializes next polygon object to be drawn as a rectangle
     *
     * @param actionEvent
     */
    private void rectangleHandler(ActionEvent actionEvent) {
        poly = new Rectangle();
    }

    /**
     * clears rectangle covering entire canvas and removes all polygon objects from shapelist
     *
     * @param actionEvent
     */
    private void clearHandler(ActionEvent actionEvent) {
        clearCanvas();
        shapeList.clear();
    }

    /**
     * goes through shapeList array, sets all colors to default colors (white fill, black outline)
     *
     * @param actionEvent
     */
    private void resetColorHandler(ActionEvent actionEvent) {
        for (int i = 0; i < shapeList.size(); i++) {
            shapeList.get(i).setFillColor(Color.WHITE);
            shapeList.get(i).setOutlineColor(Color.BLACK);
        }
        drawAll();
    }

    /**
     * deletes selected polygon from shapeList and refreshes canvas
     *
     * @param actionEvent
     */
    private void deleteShapeHandler(ActionEvent actionEvent) {
        try {
            shapeList.remove(polySelected);
            drawAll();
        } catch (NullPointerException e) {
            //catch nullpointer in case selected polygon not selected yet
            new Alert(Alert.AlertType.WARNING, "No Polygon Selected").showAndWait();
        }
    }

    /**
     * pushes polySelected to front of canvas
     *
     * @param actionEvent
     */
    private void toFrontHandler(ActionEvent actionEvent) {
        //catch nullpointer in case selected polygon not selected yet
        try {
            shapeList.remove(polySelected);
            shapeList.add(polySelected);
            drawAll();
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.WARNING, "No Polygon Selected").showAndWait();
        }
    }

    /**
     * pushes polySelected to back of canvas
     *
     * @param actionEvent
     */
    private void toBackHandler(ActionEvent actionEvent) {
        //catch nullpointer in case selected polygon not selected yet
        try {
            shapeList.remove(polySelected);
            shapeList.add(0, polySelected);
            drawAll();
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.WARNING, "No Polygon Selected").showAndWait();
        }
    }

    //handlers for mouse

    /**
     * sets initial drawn polygon properties
     * when mouse pressed first time, anchor polygon's intial x and y coordinates to that mouse pressed point,
     * polygon will be drawn from that reference point
     *
     * @param mouseEvent
     */
    private void pressHandler(MouseEvent mouseEvent) {
        //sets initial drawn polygon properties
        initPoly();
        //if left click, sets initial coordinates to where mouse first pressed for draw
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            poly.setInitCoord(mouseEvent.getX(), mouseEvent.getY());
        } else //if right click, sets initial coordinates to where mouse pressed for moving selected polygon
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                //if no polygon selected yet
                try {
                    polySelected.setInitCoord(mouseEvent.getX(), mouseEvent.getY());
                } catch (NullPointerException e) {
                    new Alert(Alert.AlertType.WARNING, "Movement Error").showAndWait();
                }
            }
    }

    /**
     * mouse drag detection
     * if left click drag detected, draw the polygon by continuously setting the x/y coordinates based on mouse position
     * if right click drag detected, find polygon in mouse coordinate in shapelist and translate that polygon with drag
     *
     * @param mouseEvent
     */
    private void dragHandler(MouseEvent mouseEvent) {
        double mouseXCoord = mouseEvent.getX();
        double mouseYCoord = mouseEvent.getY();

        //if left click drag detected to click and drag new object
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            //draw rest of polygon's coordinates based on current mouse coordinate and init coordinates
            poly.drawXCoords(mouseXCoord);
            poly.drawYCoords(mouseYCoord);
            draw(poly);
        }
        //if right click drag detected to click and drag selected object
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            //draw rest of polygon's coordinates based on difference of current mouse coordinate and init coordinates
            polySelected.translate(polySelected.getXCoords(), polySelected.getYCoords(), mouseXCoord, mouseYCoord);
            draw(polySelected);
        }
    }

    /**
     * handles when mouse is clicked
     * right click selects object
     *
     * @param mouseEvent
     */
    private void clickHandler(MouseEvent mouseEvent) {
        double mouseXCoord = mouseEvent.getX();
        double mouseYCoord = mouseEvent.getY();

        //if right click and that no drag has happened since click
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {

            //goes through coordinates of each polygon in shapelist to find if it contains coordinates of mouse
            //reverse order since front to back and arraylist is lifo
            for (int i = shapeList.size() - 1; i >= 0; i--) {

                //checks if coordinates inside polygon, if so, set polySelected to be that polygon
                if (shapeList.get(i).checkContains(mouseXCoord, mouseYCoord)) {
                    //change selected polygon outline to be red after switching previous outline color with new one
                    try {
                        //set previous selected shape's outline color to be its original outline color
                        shapeList.get(shapeList.indexOf(polySelected)).setOutlineColor(polySelectedOutline);
                        //set new selected polygon and take its outline color before setting new outline to be red
                        polySelected = shapeList.get(i);
                        polySelectedOutline = polySelected.getOutlineColor();
                        shapeList.get(i).setOutlineColor(Color.RED);

                    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                        //if first ever selected shape, or if polygon was deleted
                        //set selected polygon and take its outline before setting its new outline to red
                        polySelected = shapeList.get(i);
                        polySelectedOutline = polySelected.getOutlineColor();
                        shapeList.get(i).setOutlineColor(Color.RED);
                    }
                    //immediate mouse position inside that polygon is now new anchor for possible translate movement
                    polySelected.setInitCoord(mouseEvent.getX(), mouseEvent.getY());

                    drawAll();
                    break;
                }
            }
        }
    }

    /**
     * when mouse is released, shape is 'placed' onto canvas and added to shapelist
     *
     * @param mouseEvent
     */
    private void releaseHandler(MouseEvent mouseEvent) {
        //ensures only left click release will draw polygon
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            shapeList.add(poly);
        }

        //ensures next drawn polygon has same properties as previous one
        //initializes new polygon that has same number of points as old one just dragged
        if (poly.getPoints() == 3) {
            poly = new Triangle();
        } else if (poly.getPoints() == 4) {
            poly = new Rectangle();
        }
        //sets global variables and refreshes all shapes previously drawn before
        initPoly();
        drawAll();
    }

    /**
     * clears canvas of all graphical fills/strokes
     */
    private void clearCanvas() {
        gc.clearRect(0, 0, 1000, 400);
    }

    /**
     * takes all values from textfields/buttons and sets global variables
     * use during beginning and end of mouse press
     */
    private void initPoly() {
        gc.setFill(fillColor);
        gc.setStroke(outlineColor);
        setStrokeWidth();

        poly.setFillColor(fillColor);
        poly.setOutlineColor(outlineColor);
        poly.setStrokeWidth(strokeWidth);
    }

    /**
     * start drawingapp
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}

