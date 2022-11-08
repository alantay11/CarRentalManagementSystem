/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemwebclient;

import ws.client.partner.Partner;
import ws.client.partner.PartnerWebService_Service;

/**
 *
 * @author Uni
 */
public class HolidayReservationSystemWebClient {

    Partner currentPartner;

    public static void main(String[] args) {
        PartnerWebService_Service partnerWebService_Service = new PartnerWebService_Service();
        MainApp mainApp = new MainApp(partnerWebService_Service);
        mainApp.runApp();
    }
}
