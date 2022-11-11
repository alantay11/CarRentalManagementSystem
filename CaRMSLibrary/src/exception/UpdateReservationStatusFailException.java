/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Andrea
 */
public class UpdateReservationStatusFailException extends Exception {

    /**
     * Creates a new instance of
     * <code>UpdateReservationStatusFailException</code> without detail message.
     */
    public UpdateReservationStatusFailException() {
    }

    /**
     * Constructs an instance of
     * <code>UpdateReservationStatusFailException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public UpdateReservationStatusFailException(String msg) {
        super(msg);
    }
}
