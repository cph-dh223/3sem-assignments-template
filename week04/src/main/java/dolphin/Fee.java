package dolphin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Fee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private int amount;
    private LocalDate payDate;

    @ManyToOne
    private Person person;

    public Fee(int amount, LocalDate payDate)
    {
        this.amount = amount;
        this.payDate = payDate;
    }

}

