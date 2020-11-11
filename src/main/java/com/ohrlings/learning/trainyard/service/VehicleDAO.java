package com.ohrlings.learning.trainyard.service;

import com.ohrlings.learning.trainyard.exception.CustomException;
import com.ohrlings.learning.trainyard.model.Vehicle;

import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

public interface VehicleDAO {
    Stream getAll();
    Optional getVehicle(int id) throws Exception;
    boolean updateVehicle(Vehicle vehicle) throws CustomException, Exception;
}
