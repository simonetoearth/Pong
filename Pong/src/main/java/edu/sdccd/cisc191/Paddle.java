package edu.sdccd.cisc191;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
    private Rectangle shape;

    public Paddle(double x, double y, double width, double height) {
        shape = new Rectangle(x, y, width, height);
        shape.setFill(Color.BLUE);
    }

    public Rectangle getShape() {
        return shape;
    }

    public void move(double dy) {
        shape.setY(shape.getY() + dy);
        // Ensure paddle stays within bounds
        if (shape.getY() < 0) shape.setY(0);
        if (shape.getY() > 600 - shape.getHeight()) shape.setY(600 - shape.getHeight());
    }
}