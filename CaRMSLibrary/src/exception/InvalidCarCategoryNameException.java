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
public class InvalidCarCategoryNameException extends Exception {

    /**
     * Creates a new instance of <code>InvalidCarCategoryNameException</code>
     * without detail message.
     */
    public InvalidCarCategoryNameException() {
    }

    /**
     * Constructs an instance of <code>InvalidCarCategoryNameException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidCarCategoryNameException(String msg) {
        super(msg);
    }
}
