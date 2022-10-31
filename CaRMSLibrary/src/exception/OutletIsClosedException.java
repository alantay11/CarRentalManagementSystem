/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Uni
 */
public class OutletIsClosedException extends Exception {

    /**
     * Creates a new instance of <code>OutletIsClosedException</code> without
     * detail message.
     */
    public OutletIsClosedException() {
    }

    /**
     * Constructs an instance of <code>OutletIsClosedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OutletIsClosedException(String msg) {
        super(msg);
    }
}
