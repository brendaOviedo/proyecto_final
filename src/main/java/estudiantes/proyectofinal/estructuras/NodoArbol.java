package estudiantes.proyectofinal.estructuras;

import estudiantes.proyectofinal.modelo.Cliente;

/**
 * Nodo del árbol binario de búsqueda de clientes.
 */
public class NodoArbol {

    private final Cliente cliente;
    private NodoArbol izquierdo;
    private NodoArbol derecho;

    public NodoArbol(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public int getClave() {
        return cliente.getCodigo();
    }

    public NodoArbol getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoArbol izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoArbol getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoArbol derecho) {
        this.derecho = derecho;
    }
}
