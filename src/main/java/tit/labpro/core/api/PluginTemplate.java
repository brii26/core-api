package tit.labpro.core.api;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
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
        resizableEnabled = false;

        setStyle("-fx-background-color: transparent;");
        contentWrapper.setStyle(
            "-fx-border-width: 3; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
        );

        Rectangle clip = new Rectangle();
        contentWrapper.setClip(clip);
        contentWrapper.layoutBoundsProperty().addListener((obs, old, bounds) -> {
            clip.setWidth(bounds.getWidth());
            clip.setHeight(bounds.getHeight());
        });

        finishResizeBtn = new Button("V");
        finishResizeBtn.setFocusTraversable(false);
        finishResizeBtn.setMouseTransparent(false);
        finishResizeBtn.setVisible(false);

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

        setOnMouseClicked(e -> {
            if (!resizableEnabled) {
                enableResizeAndDrag();
            }
            e.consume();
        });

        StackPane.setAlignment(finishResizeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(finishResizeBtn, new Insets(5));
        getChildren().addAll(contentWrapper, finishResizeBtn);

        setupMouseEvents();
    }

    public StackPane getContentWrapper() {
        return contentWrapper;
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
        setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
    }

    public boolean isResizeEnabled() {
        return resizableEnabled;
    }

    public void enableResizeAndDrag() {
        resizableEnabled = true;
        finishResizeBtn.setVisible(true);
        setStyle("-fx-border-color: #8AA9F9; -fx-border-width: 3;");
    }

    protected void onResizeFinished() {
        // Optional override
    }
}
