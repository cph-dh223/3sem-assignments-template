import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ex2 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(map(new int[]{1,2,3,4}, a -> a*2)));   
        System.out.println(Arrays.toString(filter(new int[]{1,2,3,4}, a -> a % 2 == 0 ?  true : false)));
    }

    static int[] map(int[] a, MyTransformingType op){
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = op.perform(a[i]);
        }
        return b;
    }
    
    static int[] filter(int[] a, MyValidatingType op){
        List<Integer> b = new ArrayList();
        for (int i = 0; i < a.length; i++) {
            if(op.perform(a[i])){
                b.add(a[i]);
            }
        }
        int[] c = new int[b.size()];
        for (int i = 0; i < b.size(); i++) {
            c[i] = b.get(i);
        }
        return c;
        // bedere way
        // return Arrays.stream(a).filter(e -> op.perform(e)).toArray();
    }



    static String setToString(Collection a){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object object : a) {
            sb.append(sb.append(object.toString()));
            sb.append(',');
        }
        sb.delete(sb.length()-1, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
