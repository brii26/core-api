package tit.labpro.core.api;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    private final StackPane contentWrapper = new StackPane(); 

    public PluginTemplate() {
        // Outer green border wrapper
        BorderPane outerWrapper = new BorderPane();

        contentWrapper.setStyle(
            "-fx-border-width: 3; " +
            "-fx-background-color: linear-gradient(to bottom, #f0f4f7, #d9e2ec);" +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
        );

        outerWrapper.setCenter(contentWrapper);

        // Confirm button
        finishResizeBtn = new Button("V");
        finishResizeBtn.setStyle(
            "-fx-background-color: limegreen; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10;"
        );
        finishResizeBtn.setOnAction(e -> {
            disableResizeAndDrag();
            finishResizeBtn.setVisible(false);
            onResizeFinished();
        });
        finishResizeBtn.setVisible(false);

        // Place button top-right inside outer wrapper
        BorderPane topRightPane = new BorderPane();
        topRightPane.setRight(finishResizeBtn);
        topRightPane.setPickOnBounds(false);
        outerWrapper.setTop(topRightPane);

        getChildren().add(outerWrapper);

        setupMouseEvents();
    }


    public void setContent(Node node) {
        contentWrapper.getChildren().setAll(node);
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
        setStyle("-fx-border-color: #8AA9F9; -fx-border-width: 3; -fx-background-color: linear-gradient(to bottom, #f0f4f7, #d9e2ec);");
    }

    protected void onResizeFinished() {
        // Optional override
    }
}
