import com.lsxy.framework.web.web.AbstractSpringBootStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by tandy on 16/7/19.
 */
@SpringBootApplication
public class MainClass extends AbstractSpringBootStarter{

    @Override
    public String systemId() {
        return "area-server";
    }

    public static void main(String[] args) {
        SpringApplication.run(MainClass.class);
    }
}
