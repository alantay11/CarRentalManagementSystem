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
public class InvalidIdException extends Exception {

    /**
     * Creates a new instance of <code>InvalidIdException</code> without detail
     * message.
     */
    public InvalidIdException() {
    }

    /**
     * Constructs an instance of <code>InvalidIdException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidIdException(String msg) {
        super(msg);
    }
}
