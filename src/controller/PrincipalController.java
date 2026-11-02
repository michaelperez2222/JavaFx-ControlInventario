
package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.GestorInventario;
import model.Producto;

public class PrincipalController {

    @FXML private TextField txtCodigo, txtNombre, txtPrecio, txtCantidad, txtVenta;
    @FXML private TableView<Producto> tabla;
    @FXML private TableColumn<Producto, String> colCodigo, colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio, colValor;
    @FXML private TableColumn<Producto, Integer> colCantidad;

    private final GestorInventario gestor = new GestorInventario();

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        tabla.setItems(gestor.getProductos());
    }

    @FXML
    private void registrarProducto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()
                || txtPrecio.getText().trim().isEmpty() || txtCantidad.getText().trim().isEmpty()) {
            mostrar(Alert.AlertType.ERROR, "se deben llenar todos los campos al registrar un producto.");
            return;
        }
        if (codigo.contains(";") || nombre.contains(";")) {
            mostrar(Alert.AlertType.ERROR, "No se permite el carácter ';'.");
            return;
        }
        if (gestor.existeCodigo(codigo)) {
            mostrar(Alert.AlertType.ERROR, "Ya existe un producto con ese código.");
            return;
        }

        double precio;
        int cantidad;
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mostrar(Alert.AlertType.ERROR, "El precio no es un número válido.");
            return;
        }
        if (precio <= 0) {
            mostrar(Alert.AlertType.ERROR, "El precio debe ser mayor que cero.");
            return;
        }
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mostrar(Alert.AlertType.ERROR, "La cantidad debe ser un número entero.");
            return;
        }
        if (cantidad < 0) {
            mostrar(Alert.AlertType.ERROR, "La cantidad no puede ser negativa.");
            return;
        }

        try {
            gestor.agregar(new Producto(codigo, nombre, precio, cantidad));
            txtCodigo.clear();
            txtNombre.clear();
            txtPrecio.clear();
            txtCantidad.clear();
        } catch (Exception e) {
            mostrar(Alert.AlertType.ERROR, "Error al acceder al archivo: " + e.getMessage());
        }
    }

    @FXML
    private void vender() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null || !gestor.existeCodigo(seleccionado.getCodigo())) {
            mostrar(Alert.AlertType.ERROR, "Seleccione un producto de la tabla.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtVenta.getText().trim());
        } catch (NumberFormatException e) {
            mostrar(Alert.AlertType.ERROR, "Escriba una cantidad entera válida.");
            return;
        }
        if (cantidad <= 0) {
            mostrar(Alert.AlertType.ERROR, "La cantidad a vender debe ser mayor a cero.");
            return;
        }
        if (cantidad > seleccionado.getCantidad()) {
            mostrar(Alert.AlertType.ERROR, "Inventario insuficiente. Disponibles: "
                    + seleccionado.getCantidad());
            return;
        }

        try {
            double total = gestor.vender(seleccionado, cantidad);
            txtVenta.clear();
            mostrar(Alert.AlertType.INFORMATION,
                    String.format("Venta registrada. Total: RD$%,.2f", total));
        } catch (Exception e) {
            mostrar(Alert.AlertType.ERROR, "Error al intentar acceder al archivo: " + e.getMessage());
        }
    }

    @FXML
    private void verVentas() {
        try {
            FXMLLoader loader = new FXMLLoader(new File("src/view/Ventas.fxml").toURI().toURL());
            Scene scene = new Scene(loader.load(), 620, 420);
            scene.getStylesheets().add(new File("src/view/estilos.css").toURI().toString());

            Stage ventana = new Stage();
            ventana.setTitle("Ventas realizadas");
            ventana.setScene(scene);
            ventana.show();
        } catch (Exception e) {
            mostrar(Alert.AlertType.ERROR, "No se pudieron abrir las ventas: " + e.getMessage());
        }
    }

    private void mostrar(Alert.AlertType tipo, String mensaje) {
        Alert a = new Alert(tipo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}