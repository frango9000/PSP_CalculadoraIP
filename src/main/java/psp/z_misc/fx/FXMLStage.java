package psp.z_misc.fx;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLStage extends Stage {

    public FXMLStage(String title) {
        setTitle(title);
        initModality(Modality.NONE);
        initOwner(FxApplication.getMainStage());
    }

    public FXMLStage(String fxml, String title) {
        this(title);
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));

            setScene(new Scene(root));
            root.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FXMLStage(Pane root, String title) {
        this(title);
        setScene(new Scene(root));
        root.requestFocus();
    }


}
