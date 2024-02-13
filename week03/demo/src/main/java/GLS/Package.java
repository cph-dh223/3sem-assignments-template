package GLS;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    int id;
    @Column(name = "tracking_number", nullable = false)
    String trackingNumber;
    @Column(name = "sender_name", nullable = false)
    String SenderName;
    @Column(name = "receiver_name", nullable = false)
    String ReceiverName;
    @Column(name = "delivery_statuss", nullable = false)
    DeliveryStatus deliveryStatus;
    @Column(name = "timestamp", nullable = false)
    Timestamp timestamp;

    
    @PreUpdate
    @PrePersist
    public void updateTimeStampToNow(){
        timestamp = new Timestamp(System.currentTimeMillis());
    }
    public static enum DeliveryStatus{
        PENDING, 
        IN_TRANSIT, 
        DELIVERED
    }
}
