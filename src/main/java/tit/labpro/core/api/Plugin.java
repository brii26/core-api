package tit.labpro.core.api;

import javafx.scene.Parent;

public interface Plugin {
    Parent getUI();
    default void onActivate() {}
    default void onDeactivate() {}
}
