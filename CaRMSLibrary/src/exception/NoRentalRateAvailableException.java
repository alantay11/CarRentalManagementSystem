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
public class NoRentalRateAvailableException extends Exception {

    /**
     * Creates a new instance of <code>NoRentalRateAvailableException</code>
     * without detail message.
     */
    public NoRentalRateAvailableException() {
    }

    /**
     * Constructs an instance of <code>NoRentalRateAvailableException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoRentalRateAvailableException(String msg) {
        super(msg);
    }
}
