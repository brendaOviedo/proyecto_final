package estudiantes.proyectofinal.vista;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Entrada y salida por consola. Valida que los números ingresados sean enteros
 * y que los campos no estén vacíos, mostrando mensajes claros sin detener el programa.
 */
public class Consola {

    private final Scanner scanner;

    public Consola(InputStream entrada) {
        Charset charset = System.console() != null ? System.console().charset() : Charset.defaultCharset();
        this.scanner = new Scanner(entrada, charset);
    }

    /** Lee una línea de texto (recortada). Puede devolver una cadena vacía. */
    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            throw new EntradaFinalizadaException();
        }
        return scanner.nextLine().strip();
    }

    /** Lee un número entero; repite la solicitud mientras el valor sea vacío o no numérico. */
    public int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.isEmpty()) {
                error("El valor no puede estar vacío.");
                continue;
            }
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                error("\"" + texto + "\" no es un número entero válido.");
            }
        }
    }

    public void titulo(String texto) {
        String linea = "=".repeat(Math.max(40, texto.length() + 4));
        System.out.println();
        System.out.println(linea);
        System.out.println("  " + texto);
        System.out.println(linea);
    }

    public void mostrar(String texto) {
        System.out.println(texto);
    }

    public void formato(String formato, Object... valores) {
        System.out.printf(formato, valores);
    }

    public void exito(String texto) {
        System.out.println("[OK] " + texto);
    }

    public void aviso(String texto) {
        System.out.println("[AVISO] " + texto);
    }

    public void error(String texto) {
        System.out.println("[ERROR] " + texto);
    }
}
