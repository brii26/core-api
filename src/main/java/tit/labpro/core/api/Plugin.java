package tit.labpro.core.api;

import javafx.scene.Parent;

public interface Plugin {
    Parent getUI();
    void setData(Object data);
}
