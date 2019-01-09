import com.jeesite.common.config.Global;
import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.front.utils.FrontUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName MyTest
 * @Description TODO
 * @Author xuyux
 * @Date 2018/12/6 8:59
 **/
public class MyTest {
    public static void main(String[] args) {
        System.out.println(ServletUtils.getParameter("frontAd_image"));
    }
}
