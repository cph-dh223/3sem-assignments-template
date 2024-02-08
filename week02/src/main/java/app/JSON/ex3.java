package app.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ex3 {
    public static void main(String[] args) {
       System.out.println(Arrays.toString(accountsFromJSON("src/main/java/app/JSON/account.json")));
       System.out.println();
       System.out.println(DTOtoString(UserDTO.usersToDTOs(accountsFromJSON("src/main/java/app/JSON/account.json"))));
    }

    static User[] accountsFromJSON(String path){
        File file = new File(path);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file,new TypeReference<User[]>(){});
            } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    static String DTOtoString(UserDTO[] userDTOs){
        return Arrays.toString(userDTOs);
    }
    
}
