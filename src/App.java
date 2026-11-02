import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(new File("src/view/Principal.fxml").toURI().toURL());
        Parent root = loader.load();

        Scene scene = new Scene(root, 700, 480);
        scene.getStylesheets().add(new File("src/view/estilos.css").toURI().toString());

        stage.setTitle("Control de Inventario y Ventas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}