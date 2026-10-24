package estudiantes.proyectofinal.estructuras;

import estudiantes.proyectofinal.modelo.Turno;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Cola FIFO de turnos pendientes implementada con nodos propios (lista enlazada).
 * Se encola por el final y se atiende por el frente.
 */
public class ColaTurnos {

    private Nodo<Turno> frente;
    private Nodo<Turno> fin;
    private int tamanio;

    /** Agrega un turno al final de la cola. */
    public void encolar(Turno turno) {
        Nodo<Turno> nuevo = new Nodo<>(turno);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        tamanio++;
    }

    /** Retira y devuelve el primer turno; devuelve null si la cola está vacía. */
    public Turno desencolar() {
        if (estaVacia()) {
            return null;
        }
        Turno turno = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return turno;
    }

    /** Devuelve el primer turno sin retirarlo; null si la cola está vacía. */
    public Turno verPrimero() {
        return estaVacia() ? null : frente.getDato();
    }

    /** Busca un turno por número sin modificar la cola. */
    public Turno buscar(int numero) {
        Nodo<Turno> actual = frente;
        while (actual != null) {
            if (actual.getDato().getNumero() == numero) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    /**
     * Retira de la cola el turno con el número indicado, conservando el orden de los demás.
     *
     * @return el turno retirado o null si no estaba en la cola
     */
    public Turno cancelar(int numero) {
        Nodo<Turno> anterior = null;
        Nodo<Turno> actual = frente;
        while (actual != null && actual.getDato().getNumero() != numero) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (actual == null) {
            return null;
        }
        if (anterior == null) {
            frente = actual.getSiguiente();
        } else {
            anterior.setSiguiente(actual.getSiguiente());
        }
        if (actual == fin) {
            fin = anterior;
        }
        tamanio--;
        return actual.getDato();
    }

    /** Indica si algún turno de la cola pertenece al cliente indicado. */
    public boolean contieneCliente(int codigoCliente) {
        Nodo<Turno> actual = frente;
        while (actual != null) {
            if (actual.getDato().getCodigoCliente() == codigoCliente) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /** Recorre la cola desde el frente hasta el final, aplicando la acción a cada turno. */
    public void recorrer(Consumer<Turno> accion) {
        Nodo<Turno> actual = frente;
        while (actual != null) {
            accion.accept(actual.getDato());
            actual = actual.getSiguiente();
        }
    }

    /** Copia los turnos, en orden de atención, a una lista para presentarlos. */
    public List<Turno> aLista() {
        List<Turno> turnos = new ArrayList<>();
        recorrer(turnos::add);
        return turnos;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }
}
