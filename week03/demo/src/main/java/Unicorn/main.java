package Unicorn;
public class main {
    public static void main(String[] args) {
        UniconrDAO udto = new UniconrDAO();
        Unicorn newU = new Unicorn();
        newU.setAge(10);
        newU.setName("unicorn name");
        newU.setPowerStrength(9001);
        
        Unicorn cU = udto.save(newU);
        System.out.println(cU);

        Unicorn rU = udto.findById(cU.getId());
        System.out.println(rU);

        rU.setPowerStrength(9002);
        Unicorn uU = udto.update(rU);
        System.out.println(uU);
        
        // udto.delete(uU.getId());
        udto.close();
    }
}
