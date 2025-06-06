package tit.labpro.core.api;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public abstract class PluginTemplate extends StackPane {
    private boolean resizableEnabled = true;
    private boolean resizing = false;
    private double dragOffsetX, dragOffsetY;
    private double resizeStartX, resizeStartY;
    private double startWidth, startHeight;
    private final int RESIZE_MARGIN = 10;

    private final Button finishResizeBtn;

    public PluginTemplate() {
        setStyle("-fx-border-color: darkslategray; -fx-border-width: 3; -fx-background-color: linear-gradient(to bottom, #f0f4f7, #d9e2ec);");

        finishResizeBtn = new Button("Confirm");
        finishResizeBtn.setOnAction(e -> {
            disableResizeAndDrag();
            finishResizeBtn.setVisible(false);
            onResizeFinished();
        });
        finishResizeBtn.setVisible(false);
        getChildren().add(finishResizeBtn);
        StackPane.setAlignment(finishResizeBtn, Pos.BOTTOM_CENTER);
        finishResizeBtn.setTranslateY(-10);

        setupMouseEvents();
    }

    private void setupMouseEvents() {
        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDragged);
        setOnMouseMoved(this::onMouseMoved);
        setOnMouseReleased(e -> resizing = false);
    }

    private void onMousePressed(MouseEvent e) {
        if (!resizableEnabled) return;

        if (isInResizeMargin(e)) {
            resizing = true;
            resizeStartX = e.getScreenX();
            resizeStartY = e.getScreenY();
            startWidth = getWidth();
            startHeight = getHeight();

            finishResizeBtn.setVisible(true);
        } else {
            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (!resizableEnabled) return;

        if (resizing) {
            double deltaX = e.getScreenX() - resizeStartX;
            double deltaY = e.getScreenY() - resizeStartY;
            setPrefWidth(Math.max(100, startWidth + deltaX));
            setPrefHeight(Math.max(80, startHeight + deltaY));
        } else {
            setLayoutX(e.getSceneX() - dragOffsetX);
            setLayoutY(e.getSceneY() - dragOffsetY);
        }
    }

    private void onMouseMoved(MouseEvent e) {
        if (!resizableEnabled) {
            setCursor(Cursor.DEFAULT);
            return;
        }

        if (isInResizeMargin(e)) {
            setCursor(Cursor.SE_RESIZE);
        } else {
            setCursor(Cursor.MOVE);
        }
    }

    private boolean isInResizeMargin(MouseEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();
        return mouseX >= getWidth() - RESIZE_MARGIN && mouseY >= getHeight() - RESIZE_MARGIN;
    }

    public void disableResizeAndDrag() {
        resizableEnabled = false;
        setCursor(Cursor.DEFAULT);
        setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-color: linear-gradient(to bottom, #f0f4f7, #d9e2ec);");
    }

    public boolean isResizeEnabled() {
        return resizableEnabled;
    }

    public void enableResizeAndDrag() {
        resizableEnabled = true;
        finishResizeBtn.setVisible(true);
        setStyle("-fx-border-color: darkslategray; -fx-border-width: 3; -fx-background-color: linear-gradient(to bottom, #f0f4f7, #d9e2ec);");
    }

    protected void onResizeFinished() {
        // OVERRIDde
    }
}
