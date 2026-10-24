package estudiantes.proyectofinal.estructuras;

import estudiantes.proyectofinal.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Árbol binario de búsqueda (BST) de clientes, usando el código como clave.
 * La inserción, la búsqueda y el recorrido inorden son recursivos.
 */
public class ArbolClientes {

    private NodoArbol raiz;
    private int cantidad;

    /**
     * Inserta un cliente en el árbol.
     *
     * @return false si ya existía un cliente con el mismo código
     */
    public boolean insertar(Cliente cliente) {
        if (buscar(cliente.getCodigo()) != null) {
            return false;
        }
        raiz = insertarRecursivo(raiz, cliente);
        cantidad++;
        return true;
    }

    private NodoArbol insertarRecursivo(NodoArbol nodo, Cliente cliente) {
        if (nodo == null) {
            return new NodoArbol(cliente);
        }
        if (cliente.getCodigo() < nodo.getClave()) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), cliente));
        } else {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), cliente));
        }
        return nodo;
    }

    /** Busca un cliente por código; devuelve null si no existe. */
    public Cliente buscar(int codigo) {
        return buscarRecursivo(raiz, codigo);
    }

    private Cliente buscarRecursivo(NodoArbol nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        if (codigo == nodo.getClave()) {
            return nodo.getCliente();
        }
        return codigo < nodo.getClave()
                ? buscarRecursivo(nodo.getIzquierdo(), codigo)
                : buscarRecursivo(nodo.getDerecho(), codigo);
    }

    /** Recorre el árbol en inorden (izquierdo, raíz, derecho): clientes ordenados por código. */
    public void recorrerInorden(Consumer<Cliente> accion) {
        inordenRecursivo(raiz, accion);
    }

    private void inordenRecursivo(NodoArbol nodo, Consumer<Cliente> accion) {
        if (nodo == null) {
            return;
        }
        inordenRecursivo(nodo.getIzquierdo(), accion);
        accion.accept(nodo.getCliente());
        inordenRecursivo(nodo.getDerecho(), accion);
    }

    /** Devuelve los clientes ordenados por código. */
    public List<Cliente> inorden() {
        List<Cliente> clientes = new ArrayList<>();
        recorrerInorden(clientes::add);
        return clientes;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public int getCantidad() {
        return cantidad;
    }
}
