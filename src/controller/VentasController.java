package controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.GestorInventario;

public class VentasController {

    @FXML private TableView<String[]> tablaVentas;
    @FXML private TableColumn<String[], String> colFecha, colCodigo, colProducto, colCantidad, colTotal;
    @FXML private Label lblTotal;

    @FXML
    public void initialize() {
        // Cada fila es un arreglo: [fecha, código, producto, cantidad, total]
        colFecha.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue()[0]));
        colCodigo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue()[1]));
        colProducto.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue()[2]));
        colCantidad.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue()[3]));
        colTotal.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                String.format("RD$%,.2f", Double.parseDouble(d.getValue()[4]))));

        try {
            List<String[]> ventas = new GestorInventario().leerVentas();
            tablaVentas.setItems(FXCollections.observableArrayList(ventas));

            double suma = 0;
            for (String[] v : ventas) suma += Double.parseDouble(v[4]);
            lblTotal.setText(String.format("Total generado: RD$%,.2f", suma));
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("Error al leer ventas.txt: " + e.getMessage());
            a.showAndWait();
        }
    }
}