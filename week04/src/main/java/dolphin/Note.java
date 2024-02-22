package dolphin;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Note {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    String note;
    
    LocalDate creationDate;
    
    String createdBy;
    
    @ManyToOne
    Person person;

    public Note(String note, LocalDate creationDate, String createdBy) {
        this.note = note;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
    }

    public Note(String note, LocalDate creationDate) {
        this.note = note;
        this.creationDate = creationDate;
    }

    @PrePersist
    public void prePersist(){
        createdBy = person.getName();
    }

    @Override
    public boolean equals(Object arg0) {
        if(!(arg0 instanceof Note)){
            return false;
        }
        Note other = (Note)arg0;
        if(other.id == this.id){
            return true;
        }
        // if(note == other.note && createdBy == other.createdBy){
        //     return true;
        // }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Note:{note:");
        sb.append(note + ", ");
        sb.append(creationDate.toString() + ", ");
        sb.append(createdBy + "}");
        return sb.toString();
    }
    
}
