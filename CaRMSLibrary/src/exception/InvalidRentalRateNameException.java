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
public class InvalidRentalRateNameException extends Exception {

    /**
     * Creates a new instance of <code>InvalidRentalRateNameException</code>
     * without detail message.
     */
    public InvalidRentalRateNameException() {
    }

    /**
     * Constructs an instance of <code>InvalidRentalRateNameException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidRentalRateNameException(String msg) {
        super(msg);
    }
}
