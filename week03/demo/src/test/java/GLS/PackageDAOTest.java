package GLS;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PackageDAOTest {
    private static PackageDAO packageDAO;

    @BeforeEach
    public void setUp() {
        packageDAO = new PackageDAO();
    }

    @AfterEach
    public void tearDown() {
        packageDAO.close();
    }
    @Order(0)
    @Test
    public void testPersistPackage() {
        Package.PackageBuilder pb = Package.builder();
        pb.trackingNumber("ABC123");
        pb.SenderName("Sender");
        pb.ReceiverName("Receiver");
        Package pkg = pb.build();
        pkg.setDeliveryStatus(Package.DeliveryStatus.PENDING);

        Package retrievedPackage = packageDAO.persistPackage(pkg);

        // Retrieve the package from the database and assert its existence
        Assertions.assertNotNull(retrievedPackage);
        Assertions.assertEquals("ABC123", retrievedPackage.getTrackingNumber());
    }
    @Order(1)
    @Test
    public void testRead() {

        Package retrievedPackage = packageDAO.readPakageFromTrackingNumber("ABC123");

        Assertions.assertNotNull(retrievedPackage);
        Assertions.assertEquals("ABC123", retrievedPackage.getTrackingNumber());
    }
    @Order(2)
    @Test
    public void testUpdate() {

        Package retrievedPackage = packageDAO.readPakageFromTrackingNumber("ABC123");
        retrievedPackage.setTrackingNumber("DEF456");
        Package updatedPackage = packageDAO.updatePakage(retrievedPackage);

        Assertions.assertNotNull(updatedPackage);
        Assertions.assertEquals("DEF456", updatedPackage.getTrackingNumber());
    }
    @Order(3)
    @Test
    public void testDelete() {

        Package retrievedPackage = packageDAO.readPakageFromTrackingNumber("ABC123");
        packageDAO.deletePackage(retrievedPackage.getId());
        Package deletedPackage = packageDAO.readPakage(retrievedPackage.getId());
        assertNull(deletedPackage);
        // assertThrows(RuntimeException.class, ()->{packageDAO.readPakage(retrievedPackage.getId())});
    }

}

