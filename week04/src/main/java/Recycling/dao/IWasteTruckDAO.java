package Recycling.dao;

import java.util.List;

import Recycling.modle.Driver;
import Recycling.modle.WasteTruck;

public interface IWasteTruckDAO {
    // WasteTruck
    void saveWasteTruck(String brand, String registrationNumber, int capacity);
    WasteTruck getWasteTruckById(int id);
    void setWasteTruckAvailable(WasteTruck wasteTruck, boolean available);
    void deleteWasteTruck(int id);
    void addDriverToWasteTruck(WasteTruck wasteTruck, Driver driver);
    void removeDriverFromWasteTruck(WasteTruck wasteTruck, String id);
    List<WasteTruck> getAllAvailableTrucks();
}
