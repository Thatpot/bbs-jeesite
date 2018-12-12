import com.jeesite.common.config.Global;

/**
 * @ClassName MyTest
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/6 8:59
 **/
public class MyTest {
    public static void main(String[] args) {
       String s =  Global.getConfig("sys.account.registerUser.userTypes.userTypes", "-1");
        System.out.println(s);
    }
}
