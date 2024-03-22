package tddKoans;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TDDMain {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static String greet(String string) {
        if(string == null) string = "my friend";
        return greet(new String[]{string});
    }

    public static String greet(String[] strings) {
        List<String> allShouts = Arrays.stream(strings)
            .filter(s -> s.equals(s.toUpperCase()) && !s.contains("\""))
            .flatMap(s -> Arrays.stream(s.split(", ")))
            .collect(Collectors.toList());
        List<String> allNormal = Arrays.stream(strings)
            .filter(s -> !s.equals(s.toUpperCase()) && !s.contains("\""))
            .flatMap(s -> Arrays.stream(s.split(", ")))
            .collect(Collectors.toList());
        List<String> allNormalEcxapes = Arrays.stream(strings)
            .filter(s -> !s.equals(s.toUpperCase()) && s.contains("\""))
            .map(s -> (s.replace("\"", "")))
            .collect(Collectors.toList());
        allNormal.addAll(allNormalEcxapes);
        if(allNormal.size() == 0)
            return shoutGreeting(allShouts, true);
        if(allShouts.size() == 0)
            return normalGreeting(allNormal);

        return normalGreeting(allNormal) + " " + shoutGreeting(allShouts, false);
    }

    private static String shoutGreeting(List<String> strings, boolean allSout) {
        StringBuilder sb = new StringBuilder();
        if(allSout){
            sb.append("HELLO ");
        } else {
            sb.append("AND HELLO ");
        }
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            if(!string.equals(string.toUpperCase()))
                continue;
            sb.append(string);
            if(i <  strings.size() - 2) sb.append(", ");
            
            if(i == strings.size() - 2) {
                if(strings.size() > 2) sb.append(",");
                sb.append(" AND ");
            }
        }
        sb.append("!");
        return sb.toString();
    }

    private static String normalGreeting(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello, ");
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            if(string.equals(string.toUpperCase()))
                continue;
            sb.append(string);
            if(i <  strings.size() - 2) sb.append(", ");
            
            if(i == strings.size() - 2) {
                if(strings.size() > 2) sb.append(",");
                sb.append(" and ");
            }

        }
        sb.append(".");
        return sb.toString();
    }
}