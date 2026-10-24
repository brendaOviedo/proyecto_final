package estudiantes.proyectofinal.modelo;

/**
 * Cantidades de turnos de un servicio agrupadas por estado (fila del resumen por servicio).
 */
public class ResumenServicio {

    private final int codigoServicio;
    private final String nombreServicio;
    private int pendientes;
    private int atendidos;
    private int cancelados;

    public ResumenServicio(int codigoServicio, String nombreServicio) {
        this.codigoServicio = codigoServicio;
        this.nombreServicio = nombreServicio;
    }

    public void contar(EstadoTurno estado) {
        switch (estado) {
            case PENDIENTE -> pendientes++;
            case ATENDIDO -> atendidos++;
            case CANCELADO -> cancelados++;
        }
    }

    /** Acumula en este resumen las cantidades de otro (útil para la fila de totales). */
    public void sumar(ResumenServicio otro) {
        pendientes += otro.pendientes;
        atendidos += otro.atendidos;
        cancelados += otro.cancelados;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public int getPendientes() {
        return pendientes;
    }

    public int getAtendidos() {
        return atendidos;
    }

    public int getCancelados() {
        return cancelados;
    }

    public int getTotal() {
        return pendientes + atendidos + cancelados;
    }
}
