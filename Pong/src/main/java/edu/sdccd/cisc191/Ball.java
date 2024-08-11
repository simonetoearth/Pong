package edu.sdccd.cisc191;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
    private Circle shape;
    private double dx, dy;
    private static final double INITIAL_DX = 3.0;
    private static final double INITIAL_DY = 3.0;

    public Ball(double x, double y, double radius) {
        shape = new Circle(x, y, radius);
        shape.setFill(Color.RED);
        reset();
    }

    public Circle getShape() {
        return shape;
    }

    public void move() {
        shape.setCenterX(shape.getCenterX() + dx);
        shape.setCenterY(shape.getCenterY() + dy);
    }

    public void bounceVertical() {
        dy = -dy;
    }

    public void bounceHorizontal() {
        dx = -dx;
    }

    public void reset() {
        shape.setCenterX(400);
        shape.setCenterY(300);
        dx = INITIAL_DX * (Math.random() < 0.5 ? 1 : -1); // Random initial direction
        dy = INITIAL_DY * (Math.random() < 0.5 ? 1 : -1); // Random initial direction
    }

    public boolean isOutOfBounds() {
        return shape.getCenterX() < 0 || shape.getCenterX() > 800;
    }
}