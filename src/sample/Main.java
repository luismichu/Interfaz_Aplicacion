package sample;

import Data.Factura;
import JSONmanager.JSONparser;
import XMLmanager.XMLparser;
import XMLmanager.XMLreader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Date;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Factura factura = new Factura("CIF1", "RAZ1", 4
                , "Desc factura", 4f, 4f, 4f, new Date(), new Date());
        //JSONparser.parseFactura(factura);
        //XMLparser.parseFactura(factura);
        //DataBase.insertFactura(factura);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        Controller controller = (Controller)loader.getController();

        primaryStage.setTitle("Aplicación de escritorio");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setResizable(false);

        controller.setStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
