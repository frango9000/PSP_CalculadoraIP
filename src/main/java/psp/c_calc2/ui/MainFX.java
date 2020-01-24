package psp.c_calc2.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainFX extends Application {

    private static Stage mainStage;

    public static void main(String[] args) {
        launch(args);

    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        MainFX.mainStage = mainStage;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        setMainStage(primaryStage);

        Pane root = new LauncherPane();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
