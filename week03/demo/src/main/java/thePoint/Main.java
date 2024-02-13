package thePoint;

public class Main {
    public static void main(String[] args) {
        PointDAO pdto = new PointDAO();

        // Store 1000 Point objects in the database:
        pdto.createPoints(1000);

        // Find the number of Point objects in the database:
        pdto.numPoints();

        // Find the average X value:
        pdto.avgX();
        
        // Retrieve all the Point objects from the database:
        pdto.getAllPoints().forEach(System.out::println);;
        
        // Close the database connection:
        pdto.close();
    }

}

