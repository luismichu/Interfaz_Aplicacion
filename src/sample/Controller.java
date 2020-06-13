package sample;

import Data.Factura;
import Data.Proveedor;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Controller{
    @FXML
    public TabPane tabPane;
    private Stage stage;

    public void initialize(){
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tFacturas = new Tab("Facturas");
        BorderPane pane1 = new BorderPane();
        Button btImportar = new Button("Importar");
        Button btBorrar = new Button("Eliminar");
        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 50, 20, 40));
        hb.setSpacing(10);
        hb.getChildren().addAll(btImportar, btBorrar);
        pane1.setTop(hb);

        ListView<Factura> listViewFacturas = new ListView<>();
        listViewFacturas.setMinHeight(490);
        VBox vb = new VBox();
        vb.setPadding(new Insets(0, 0, 0, 40));
        vb.getChildren().add(listViewFacturas);
        pane1.setLeft(vb);

        if(DataBase.getFacturas() != null && !DataBase.getFacturas().isEmpty())
            listViewFacturas.getItems().addAll(DataBase.getFacturas());

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
        hb = new HBox();
        hb.setPadding(new Insets(20, 50, 20, 40));
        hb.setSpacing(10);
        hb.getChildren().addAll(btNuevo, btEditar, btEliminar);
        pane2.setTop(hb);

        final ListView<Proveedor> listViewProveedores = new ListView<>();
        listViewProveedores.setMinHeight(490);
        vb = new VBox();
        vb.setPadding(new Insets(0, 0, 0, 40));
        vb.getChildren().add(listViewProveedores);
        pane2.setLeft(vb);

        if(DataBase.getProveedores() != null && !DataBase.getProveedores().isEmpty())
            listViewProveedores.getItems().addAll(DataBase.getProveedores());

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
                fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "\\Facturas"));
                File file = fileChooser.showOpenDialog(stage);
                Factura factura = new Factura();
                if(file != null && file.exists()) {
                    if (file.toString().endsWith(".xml"))
                        factura = XMLreader.readFactura(file.toString());
                    else if (file.toString().endsWith(".json"))
                        factura = JSONreader.readFactura(file.toString());

                    Proveedor proveedor = DataBase.getProveedor(factura.getCIF_PROVEEDOR());
                    if(proveedor == null)
                        error("El proveedor " + factura.getCIF_PROVEEDOR() + " no existe");
                    else {
                        DataBase.insertFactura(factura);
                        listViewFacturas.getItems().clear();
                        listViewFacturas.getItems().addAll(DataBase.getFacturas());
                    }
                }
            }
        });

        btBorrar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                tabPane.requestFocus();
                int selectedItem = listViewFacturas.getSelectionModel().getSelectedItem().getNUM_FACTURA();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Eliminar");
                alert.setHeaderText("¿Seguro que desea eliminar la factura " + selectedItem + "?");

                if(alert.showAndWait().get() == ButtonType.OK){
                    DataBase.eliminarFactura(selectedItem);
                    listViewFacturas.getItems().clear();
                    if(DataBase.getFacturas() != null && !DataBase.getFacturas().isEmpty())
                        listViewFacturas.getItems().addAll(DataBase.getFacturas());
                    textAreaFactura.setText("");
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminado");
                    alert.setHeaderText("Se ha eliminado la factura " + selectedItem + " correctamente");
                    alert.showAndWait();
                }
            }
        });

        listViewFacturas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Factura>() {
            @Override
            public void changed(ObservableValue<? extends Factura> observable, Factura oldValue, Factura newValue) {
                if(newValue != null)
                    textAreaFactura.setText("Factura " + newValue.getNUM_FACTURA() + "\n" +
                        "CIF del proveedor: " + newValue.getCIF_PROVEEDOR() + "\n" +
                        "Razon del Proveedor: " + newValue.getRAZ_PROVEEDOR() + "\n" +
                        "Descripcion de la factura: " + newValue.getDES_FACTURA() + "\n" +
                        "Base Imponible: " + newValue.getBAS_IMPONIBLE() + "\n" +
                        "Importe del IVA: " + newValue.getIVA_IMPORTE() + "\n" +
                        "Importe total: " + newValue.getTOT_IMPORTE() + "\n" +
                        "Fecha de la factura: " + newValue.getFEC_FACTURA() + "\n" +
                        "Fecha de vencimiento: " + newValue.getFEC_VENCIMIENTO());

            }
        });

        listViewProveedores.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Proveedor>() {
            @Override
            public void changed(ObservableValue<? extends Proveedor> observable, Proveedor oldValue, Proveedor newValue) {
                if(newValue != null)
                    textAreaProveedor.setText("Proveedor\n" +
                            "CIF del proveedor: " + newValue.getCIF_PROVEEDOR() + "\n" +
                            "Razon del Proveedor: " + newValue.getRAZ_PROVEEDOR() + "\n" +
                            "Registro notarial: " + newValue.getREG_NOTARIAL() + "\n" +
                            "Seguro de responsabilidad: " + newValue.getSEG_RESPONSABILIDAD() + "\n" +
                            "Importe del seguro: " + newValue.getSEG_IMPORTE() + "\n" +
                            "Fecha de homologacion: " + newValue.getFEC_HOMOLOGACION());
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
                Dialog<Proveedor> dialog = new Dialog<>();
                dialog.setTitle("Nuevo Proveedor");
                dialog.setHeaderText("Introduzca un nuevo proveedor");
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                ArrayList<TextField> campos = new ArrayList<>();

                TextField elem = new TextField();
                elem.setPromptText("CIF del Proveedor");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Razon del Proveedor");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Registro Notarial");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Seguro de Responsabilidad");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Importe del Seguro");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Fecha de Homologacion");
                campos.add(elem);
                elem = new PasswordField();
                elem.setPromptText("Codigo de autorizacion");
                campos.add(elem);

                for(int i = 0; i < campos.size(); i++)
                    grid.add(campos.get(i), 0, i);

                final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                btOk.addEventFilter(
                        ActionEvent.ACTION,
                        event -> {
                            Proveedor proveedor = new Proveedor();
                            try {
                                for(TextField t : campos) {
                                    if (t.getText().isEmpty())
                                        throw new Exception();
                                }
                                proveedor.setCIF_PROVEEDOR(campos.get(0).getText());
                                proveedor.setRAZ_PROVEEDOR(campos.get(1).getText());
                                proveedor.setREG_NOTARIAL(Integer.parseInt(campos.get(2).getText()));
                                proveedor.setSEG_RESPONSABILIDAD(campos.get(3).getText());
                                proveedor.setSEG_IMPORTE((int)(Double.parseDouble(campos.get(4).getText())));
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                proveedor.setFEC_HOMOLOGACION(format.parse(campos.get(5).getText()));

                                DataBase.insertProveedor(proveedor);
                                listViewProveedores.getItems().clear();
                                listViewProveedores.getItems().addAll(DataBase.getProveedores());
                            }catch(Exception exc){
                                exc.printStackTrace();
                                event.consume();
                                error("Comprueba el formato de los datos");
                            }
                        }
                );


                dialog.getDialogPane().setContent(grid);
                dialog.showAndWait();
            }
        });

        btEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Proveedor proveedor = listViewProveedores.getSelectionModel().getSelectedItem();
                Dialog<ArrayList<String>> dialog = new Dialog<>();
                dialog.setTitle("Editar Proveedor");
                dialog.setHeaderText("Edite el proveedor " + proveedor);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                ArrayList<TextField> campos = new ArrayList<>();

                TextField elem = new TextField();
                elem.setPromptText("CIF del Proveedor");
                elem.setDisable(true);
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Razon del Proveedor");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Registro Notarial");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Seguro de Responsabilidad");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Importe del Seguro");
                campos.add(elem);
                elem = new TextField();
                elem.setPromptText("Fecha de Homologacion");
                campos.add(elem);
                elem = new PasswordField();
                elem.setPromptText("Codigo de autorizacion");
                campos.add(elem);

                for(int i = 0; i < campos.size(); i++)
                    grid.add(campos.get(i), 0, i);

                campos.get(0).setText(proveedor.getCIF_PROVEEDOR());
                campos.get(1).setText(proveedor.getRAZ_PROVEEDOR());
                campos.get(2).setText(String.valueOf(proveedor.getREG_NOTARIAL()));
                campos.get(3).setText(proveedor.getSEG_RESPONSABILIDAD());
                campos.get(4).setText(String.valueOf(proveedor.getSEG_IMPORTE()));
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                campos.get(5).setText(format.format(proveedor.getFEC_HOMOLOGACION()));

                final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                btOk.addEventFilter(
                        ActionEvent.ACTION,
                        event -> {
                            Proveedor nuevoProveedor = new Proveedor();
                            try {
                                for(TextField t : campos) {
                                    if (t.getText().isEmpty())
                                        throw new Exception();
                                }
                                nuevoProveedor.setCIF_PROVEEDOR(campos.get(0).getText());
                                nuevoProveedor.setRAZ_PROVEEDOR(campos.get(1).getText());
                                nuevoProveedor.setREG_NOTARIAL(Integer.parseInt(campos.get(2).getText()));
                                nuevoProveedor.setSEG_RESPONSABILIDAD(campos.get(3).getText());
                                nuevoProveedor.setSEG_IMPORTE((int)(Double.parseDouble(campos.get(4).getText())));
                                nuevoProveedor.setFEC_HOMOLOGACION(format.parse(campos.get(5).getText()));

                                DataBase.updateProveedor(nuevoProveedor);
                                listViewProveedores.getItems().clear();
                                listViewProveedores.getItems().addAll(DataBase.getProveedores());
                                textAreaProveedor.setText("");
                            }catch(Exception exc){
                                event.consume();
                                error("Comprueba el formato de los datos");
                            }
                        }
                );

                dialog.getDialogPane().setContent(grid);
                dialog.showAndWait();
            }
        });

        btEliminar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                tabPane.requestFocus();
                String selectedItem = listViewProveedores.getSelectionModel().getSelectedItem().getCIF_PROVEEDOR();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Eliminar");
                alert.setHeaderText("¿Seguro que desea eliminar" + selectedItem + "?");

                if(alert.showAndWait().get() == ButtonType.OK){
                    DataBase.eliminarProveedor(selectedItem);
                    listViewProveedores.getItems().clear();
                    if(DataBase.getProveedores() != null && !DataBase.getProveedores().isEmpty())
                        listViewProveedores.getItems().addAll(DataBase.getProveedores());
                    textAreaProveedor.setText("");
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminado");
                    alert.setHeaderText("Se ha eliminado " + selectedItem + " correctamente");
                    alert.showAndWait();
                }
            }
        });
    }

    public static void error(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.showAndWait();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
}
