package app;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ex1{
    public static MyTransformingType doubleValue = (x) -> x * 2;

    public static void main(String[] args) {

        System.out.println(operate(3, 2, (a, b) -> a + b));
        System.out.println(operate(3, 2, (a, b) -> a * b));
        int[] tmp = operate(new int[]{1,2,3}, new int[]{1,2,3}, (a, b) -> a + b);
        for (int i = 0; i < 3; i++) {
            System.out.print(tmp[i] + ",");
        }
        System.out.println();
        tmp = operate(new int[]{1,2,3}, new int[]{1,2,3}, (a, b) -> a * b);
        for (int i = 0; i < 3; i++) {
            System.out.print(tmp[i] + ",");
        }
        System.out.println();
    }

    static int operate (int a, int b, ArithmeticOperation op){
        return op.perform(a, b);
    }

    static int[] operate (int[] a, int[] b, ArithmeticOperation op){
        int[] c = new int[a.length];
        for (int i = 0; i < b.length; i++) {
            c[i] = op.perform(a[i], b[i]);
        }
        return c;
    }

}