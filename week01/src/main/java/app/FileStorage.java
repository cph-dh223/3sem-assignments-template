package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

public class FileStorage<T extends Serializable> implements DataStorage<T> {


    @Override
    public String store(T data) {
        String dataSource = "" + data.hashCode() + ".txt";
        try(FileOutputStream file = new FileOutputStream(dataSource)){
            try(ObjectOutputStream out = new ObjectOutputStream(file)){
                out.writeObject(data);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return dataSource;
    }

    @Override
    public T retrieve(String source) {
        try(FileInputStream file = new FileInputStream(source)){
            try (ObjectInputStream objIn = new ObjectInputStream(file)) {
                return (T) objIn.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
