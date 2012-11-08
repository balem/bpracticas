/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpracticas;

public class PropertyException extends Exception {

    public PropertyException(String mensaje) {
        super(mensaje);
    }

    public PropertyException() {
        super();
    }

    public PropertyException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public PropertyException(Throwable mensaje) {
        super(mensaje);
    }
}
