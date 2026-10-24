package estudiantes.proyectofinal.modelo;

/**
 * Cliente de la oficina. El código es la clave del BST y no puede modificarse.
 */
public class Cliente {

    private final int codigo;
    private String nombre;
    private String telefono;
    private boolean activo;

    public Cliente(int codigo, String nombre, String telefono, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.activo = activo;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getEstadoTexto() {
        return activo ? "Activo" : "Inactivo";
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (Tel. " + telefono + ", " + getEstadoTexto() + ")";
    }
}
