/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import exception.CarModelExistException;
import exception.InputDataValidationException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Andrea
 */
@Remote
public interface CarModelSessionBeanRemote {
    public CarModel createCarModel(CarModel carModel) throws CarModelExistException, InputDataValidationException;

    public List<CarModel> retrieveAllCarModels();

    public CarModel updateCarModel(CarModel carModel) throws InputDataValidationException;

    public CarModel retrieveCarModel(long carModelId);

    public boolean deleteCarModel(long carModelId);
}
