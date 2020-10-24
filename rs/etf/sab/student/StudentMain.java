package rs.etf.sab.student;

import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        AddressOperations addressOperations = new ss140588_AddressOperations(); // Change this to your implementation.
        CityOperations cityOperations = new ss140588_CityOperations(); // Do it for all classes.
        CourierOperations courierOperations = new ss140588_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new ss140588_CourierRequestOperation();
        DriveOperation driveOperation = new ss140588_DriveOperation();
        GeneralOperations generalOperations = new ss140588_GeneralOperations();
        PackageOperations packageOperations = new ss140588_PackageOperations();
        StockroomOperations stockroomOperations = new ss140588_StockroomOperations();
        UserOperations userOperations = new ss140588_UserOperations();
        VehicleOperations vehicleOperations = new ss140588_VehicleOperations();
        

        TestHandler.createInstance(
                addressOperations,
                cityOperations,
                courierOperations,
                courierRequestOperation,
                driveOperation,
                generalOperations,
                packageOperations,
                stockroomOperations,
                userOperations,
                vehicleOperations);

        TestRunner.runTests();
    }
}
