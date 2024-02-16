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
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column(name = "sender_name", nullable = false)
    private String SenderName;

    @Column(name = "receiver_name", nullable = false)
    private String ReceiverName;

    @Column(name = "delivery_statuss", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    
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
