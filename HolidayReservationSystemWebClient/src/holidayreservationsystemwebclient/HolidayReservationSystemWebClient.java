/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemwebclient;

import java.util.Scanner;
import ws.client.partner.InvalidLoginCredentialException;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.Partner;
import ws.client.partner.PartnerWebService;
import ws.client.partner.PartnerWebService_Service;

/**
 *
 * @author Uni
 */
public class HolidayReservationSystemWebClient {

    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        PartnerWebService_Service partnerWebService_Service = new PartnerWebService_Service();
        PartnerWebService partnerWebServicePort = partnerWebService_Service.getPartnerWebServicePort();
        
        Partner currentPartner;
        
        
        
        
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** HRSWC System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                currentPartner = partnerWebServicePort.partnerLogin(username, password);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginCredentialException_Exception ex) {
                System.out.println("\nInvalid login credential: " + ex.getMessage() + "\n");
            } 
        } else {
            System.out.println("Invalid input, please try again!\n");
        }
        
        
    }
    
}
