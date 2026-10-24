package estudiantes.proyectofinal.modelo;

import java.time.LocalDateTime;

/**
 * Turno solicitado por un cliente para un servicio.
 * La fecha de finalización corresponde a la atención o a la cancelación.
 */
public class Turno {

    private final int numero;
    private final int codigoCliente;
    private final int codigoServicio;
    private final LocalDateTime fechaGeneracion;
    private EstadoTurno estado;
    private LocalDateTime fechaFinalizacion;

    public Turno(int numero, int codigoCliente, int codigoServicio, LocalDateTime fechaGeneracion,
            EstadoTurno estado, LocalDateTime fechaFinalizacion) {
        this.numero = numero;
        this.codigoCliente = codigoCliente;
        this.codigoServicio = codigoServicio;
        this.fechaGeneracion = fechaGeneracion;
        this.estado = estado;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public int getNumero() {
        return numero;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public boolean estaPendiente() {
        return estado == EstadoTurno.PENDIENTE;
    }

    public void marcarAtendido(LocalDateTime fecha) {
        finalizar(EstadoTurno.ATENDIDO, fecha);
    }

    public void marcarCancelado(LocalDateTime fecha) {
        finalizar(EstadoTurno.CANCELADO, fecha);
    }

    private void finalizar(EstadoTurno nuevoEstado, LocalDateTime fecha) {
        if (!estaPendiente()) {
            throw new IllegalStateException("El turno " + numero + " ya no está pendiente.");
        }
        this.estado = nuevoEstado;
        this.fechaFinalizacion = fecha;
    }

    @Override
    public String toString() {
        return "Turno #" + numero + " [cliente " + codigoCliente + ", servicio " + codigoServicio + ", " + estado + "]";
    }
}
