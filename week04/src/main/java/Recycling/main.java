package Recycling;

import java.math.BigDecimal;

import Recycling.config.HibernateConfig;
import Recycling.dao.DriverDAO;
import jakarta.persistence.EntityManagerFactory;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig("dolphin", false);
        DriverDAO ddao = new DriverDAO(emf);
        
        System.out.println(ddao.fetchDriverWithHighestSalary());       
    }
    
    
}
