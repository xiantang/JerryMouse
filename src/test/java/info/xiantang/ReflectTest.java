package info.xiantang;

/**
 * Hello world!
 *
 */
class Iphone{
    public int a = 1;
}
public class ReflectTest

{

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class aa = Class.forName("info.xiantang.Iphone");
        System.out.println(aa.newInstance());;

    }
}
