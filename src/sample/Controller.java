package sample;

import Data.Factura;
import JSONmanager.JSONreader;
import XMLmanager.XMLreader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Controller {
    @FXML
    public TabPane tabPane;
    private Stage stage;

    public void initialize(){
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tFacturas = new Tab("Facturas");
        BorderPane pane1 = new BorderPane();
        Button btImportar = new Button("Importar");
        VBox vb = new VBox();
        vb.setPadding(new Insets(20, 50, 20, 40));
        vb.getChildren().add(btImportar);
        pane1.setTop(vb);

        ListView<String> listViewFacturas = new ListView<>();
        listViewFacturas.setMinHeight(490);
        vb = new VBox();
        vb.setPadding(new Insets(0, 0, 0, 40));
        vb.getChildren().add(listViewFacturas);
        pane1.setLeft(vb);

        final TextArea textAreaFactura = new TextArea();
        textAreaFactura.setEditable(false);
        textAreaFactura.setFocusTraversable(false);
        textAreaFactura.setMouseTransparent(true);
        textAreaFactura.setMinHeight(490);
        textAreaFactura.setMaxWidth(650);
        vb = new VBox();
        vb.setPadding(new Insets(0, 0, 10, 40));
        vb.getChildren().add(textAreaFactura);
        pane1.setCenter(vb);

        tFacturas.setContent(pane1);

        Tab tProveedores = new Tab("Proveedores");
        BorderPane pane2 = new BorderPane();
        Button btNuevo = new Button("Nuevo");
        final Button btEditar = new Button("Editar");
        btEditar.setDisable(true);
        final Button btEliminar = new Button("Eliminar");
        btEliminar.setDisable(true);
        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 50, 20, 40));
        hb.setSpacing(10);
        hb.getChildren().addAll(btNuevo, btEditar, btEliminar);
        pane2.setTop(hb);

        final ListView<String> listViewProveedores = new ListView<>();
        listViewProveedores.setMinHeight(490);
        for (int i = 0; i < 50; i++)
            listViewProveedores.getItems().add("Proveedor " + i);
        vb = new VBox();
        vb.setPadding(new Insets(0, 0, 0, 40));
        vb.getChildren().add(listViewProveedores);
        pane2.setLeft(vb);

        final TextArea textAreaProveedor = new TextArea();
        textAreaProveedor.setEditable(false);
        textAreaProveedor.setFocusTraversable(false);
        textAreaProveedor.setMouseTransparent(true);
        textAreaProveedor.setMinHeight(490);
        textAreaProveedor.setMaxWidth(650);
        vb = new VBox();
        vb.setPadding(new Insets(0, 0, 10, 40));
        vb.getChildren().add(textAreaProveedor);
        pane2.setCenter(vb);

        tProveedores.setContent(pane2);

        tabPane.getTabs().addAll(tFacturas, tProveedores);

        btImportar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Importar archivo");
                File file = fileChooser.showOpenDialog(stage);
                Factura factura = new Factura();
                if(file.exists()) {
                    if (file.toString().endsWith(".xml"))
                        factura = XMLreader.readFactura(file.toString());
                    else if (file.toString().endsWith(".json"))
                        factura = JSONreader.readFactura(file.toString());

                    listViewFacturas.getItems().add("Factura " + factura.getNUM_FACTURA());
                }
            }
        });

        listViewFacturas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textAreaFactura.setText("Factura:\n" + newValue);
            }
        });

        listViewProveedores.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textAreaProveedor.setText("Proveedor:\n" + newValue);
            }
        });
        listViewProveedores.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                btEditar.setDisable(!newValue);
                btEliminar.setDisable(!newValue);
            }
        });

        btNuevo.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Dialog<ArrayList<String>> dialog = new Dialog<>();
                dialog.setTitle("Nuevo Proveedor");
                dialog.setHeaderText("Introduzca un nuevo proveedor");
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField elem1 = new TextField();
                elem1.setPromptText("elem1");
                TextField elem2 = new TextField();
                elem2.setPromptText("elem2");
                TextField elem3 = new TextField();
                elem3.setPromptText("elem3");

                grid.add(new Label("Elem1:"), 0, 0);
                grid.add(elem1, 1, 0);
                grid.add(new Label("Elem2:"), 0, 1);
                grid.add(elem2, 1, 1);
                grid.add(new Label("Elem3:"), 0, 2);
                grid.add(elem3, 1, 2);

                dialog.setResultConverter(new Callback<ButtonType, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> call(ButtonType param) {
                        if(param == ButtonType.OK){
                            ArrayList<String> arrayList = new ArrayList<String>();
                            arrayList.add(elem1.getText());
                            arrayList.add(elem2.getText());
                            arrayList.add(elem3.getText());
                            return arrayList;
                        }
                        return null;
                    }
                });

                dialog.getDialogPane().setContent(grid);
                Optional<ArrayList<String>> arrayList = dialog.showAndWait();
                arrayList.ifPresent(System.out::println);
            }
        });

        btEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String selectedItem = listViewProveedores.getSelectionModel().getSelectedItem();
                Dialog<ArrayList<String>> dialog = new Dialog<>();
                dialog.setTitle("Editar Proveedor");
                dialog.setHeaderText("Edite el proveedor " + selectedItem);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField elem1 = new TextField();
                elem1.setPromptText("elem1");
                TextField elem2 = new TextField();
                elem2.setPromptText("elem2");
                TextField elem3 = new TextField();
                elem3.setPromptText("elem3");

                elem1.setText("dato1");
                elem2.setText("dato2");
                elem3.setText("dato3");

                grid.add(new Label("Elem1:"), 0, 0);
                grid.add(elem1, 1, 0);
                grid.add(new Label("Elem2:"), 0, 1);
                grid.add(elem2, 1, 1);
                grid.add(new Label("Elem3:"), 0, 2);
                grid.add(elem3, 1, 2);

                dialog.setResultConverter(new Callback<ButtonType, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> call(ButtonType param) {
                        if(param == ButtonType.OK){
                            ArrayList<String> arrayList = new ArrayList<String>();
                            arrayList.add(elem1.getText());
                            arrayList.add(elem2.getText());
                            arrayList.add(elem3.getText());
                            return arrayList;
                        }
                        return null;
                    }
                });

                dialog.getDialogPane().setContent(grid);
                Optional<ArrayList<String>> arrayList = dialog.showAndWait();
                arrayList.ifPresent(System.out::println);
            }
        });

        btEliminar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                tabPane.requestFocus();
                String selectedItem = listViewProveedores.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Eliminar");
                alert.setHeaderText("¿Seguro que desea eliminar" + selectedItem + "?");

                if(alert.showAndWait().get() == ButtonType.OK){
                    listViewProveedores.getItems().remove(selectedItem);
                    textAreaProveedor.setText("");
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminado");
                    alert.setHeaderText("Se ha eliminado " + selectedItem + " correctamente");
                    alert.showAndWait();
                }
            }
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
}
