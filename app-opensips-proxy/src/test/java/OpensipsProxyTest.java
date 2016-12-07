import com.lsxy.app.opensips.OpensipsMain;
import com.lsxy.call.center.api.opensips.service.OpensipsExtensionService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liups on 2016/11/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(OpensipsMain.class)
public class OpensipsProxyTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Autowired
    OpensipsExtensionService opensipsExtensionService;
    @Test
    public void testOpensipsProxy(){
        opensipsExtensionService.createExtension("456791132","123456");
    }

}
