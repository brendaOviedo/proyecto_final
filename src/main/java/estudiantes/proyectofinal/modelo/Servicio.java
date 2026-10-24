package estudiantes.proyectofinal.modelo;

/**
 * Servicio que ofrece la oficina. El código no puede modificarse.
 */
public class Servicio {

    private final int codigo;
    private String nombre;
    private boolean activo;

    public Servicio(int codigo, String nombre, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
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
        return codigo + " - " + nombre + " (" + getEstadoTexto() + ")";
    }
}
