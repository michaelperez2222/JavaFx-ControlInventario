package model;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;

    public Producto(String codigo, String nombre, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getCodigo() { 
        return codigo;
     }
    public String getNombre() { 
        return nombre; 
    }
    public double getPrecio() {
         return precio; 
    }
    public int getCantidad() {
         return cantidad;
    }

    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
    }

    // Valor del producto en inventario, precio × cantidad
    public double getValorTotal() {
        return precio * cantidad;
    }

    public String aLinea() {
        return codigo + ";" + nombre + ";" + precio + ";" + cantidad;
    }

    public static Producto desdeLinea(String linea) {
        String[] p = linea.split(";");
        return new Producto(p[0], p[1], Double.parseDouble(p[2]), Integer.parseInt(p[3]));
    }
}