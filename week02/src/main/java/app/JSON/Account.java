package app.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Account {
    // Why crash when private?!?
    public String id;
    public String balance;
    public boolean isActive;
    
    public Account(){}
    public Account(String id, String balance, boolean isActive) {
        this.id = id;
        this.balance = balance;
        this.isActive = isActive;
    }
    
}
