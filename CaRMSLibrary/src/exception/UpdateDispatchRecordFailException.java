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
public class UpdateDispatchRecordFailException extends Exception {

    /**
     * Creates a new instance of <code>UpdateDispatchRecordFailException</code>
     * without detail message.
     */
    public UpdateDispatchRecordFailException() {
    }

    /**
     * Constructs an instance of <code>UpdateDispatchRecordFailException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateDispatchRecordFailException(String msg) {
        super(msg);
    }
}
