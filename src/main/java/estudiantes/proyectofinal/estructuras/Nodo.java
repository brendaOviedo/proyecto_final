package estudiantes.proyectofinal.estructuras;

/**
 * Nodo de una lista simplemente enlazada.
 *
 * @param <T> tipo del dato almacenado
 */
public class Nodo<T> {

    private final T dato;
    private Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
