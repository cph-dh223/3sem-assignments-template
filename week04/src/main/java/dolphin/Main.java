package dolphin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Dolphin!");

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig("dolphin", false);
        try(EntityManager em = emf.createEntityManager())
        {
            Person p1 = new Person("Hanzi");
            PersonDetail pd1 = new PersonDetail("Algade 3", 4300, "Holbæk", 45);
            p1.addPersonDetail(pd1);
            Fee f1 = new Fee(125, LocalDate.of(2023, 8, 25));
            Fee f2 = new Fee(150, LocalDate.of(2023, 7, 19));
            p1.addFee(f1);
            p1.addFee(f2);
            p1.setNotes(List.of(new Note("hello", LocalDate.of(2023, 8, 25), p1.getName()),new Note("world", LocalDate.of(2023, 8, 25), p1.getName())));
            em.getTransaction().begin();
            em.persist(p1);
            em.getTransaction().commit();
            
        }
    }
}