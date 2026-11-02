package model;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestorInventario {

    private static final String PRODUCTOS = "productos.txt";
    private static final String VENTAS = "ventas.txt";

    private final ObservableList<Producto> productos = FXCollections.observableArrayList();

    public GestorInventario() {
        cargar();
    }

    public ObservableList<Producto> getProductos() {
        return productos;
    }

    public boolean existeCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) return true;
        }
        return false;
    }

    public void agregar(Producto p) throws IOException {
        productos.add(p);
        guardar();
    }

    // Procesa una venta: descuenta stock, actualiza productos.txt y se registra en ventas.txt
    public double vender(Producto producto, int cantidad) throws IOException {
        double total = producto.getPrecio() * cantidad;

        producto.setCantidad(producto.getCantidad() - cantidad);
        guardar();

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(VENTAS, true))) {
            bw.write(fecha + ";" + producto.getCodigo() + ";" + producto.getNombre()
                    + ";" + cantidad + ";" + total);
            bw.newLine();
        }
        // Fuerza el refresco de la tabla (la cantidad cambió dentro del objeto)
        int i = productos.indexOf(producto);
        productos.set(i, producto);
        return total;
    }

    // Devuelve las líneas de ventas.txt como arreglos de texto
    public List<String[]> leerVentas() throws IOException {
        List<String[]> ventas = new ArrayList<>();
        File f = new File(VENTAS);
        if (!f.exists()) return ventas;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length == 5) ventas.add(partes);
            }
        }
        return ventas;
    }

    private void cargar() {
        File f = new File(PRODUCTOS);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    productos.add(Producto.desdeLinea(linea));
                } catch (Exception ex) {
                    System.out.println("Línea ignorada: " + linea);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al leer productos: " + ex.getMessage());
        }
    }

    private void guardar() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRODUCTOS))) {
            for (Producto p : productos) {
                bw.write(p.aLinea());
                bw.newLine();
            }
        }
    }
}