package school.dao;

import jakarta.persistence.EntityManagerFactory;

public abstract class absDAO {
    public EntityManagerFactory emf;
    public absDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public void close(){
        emf.close();
    }
}
