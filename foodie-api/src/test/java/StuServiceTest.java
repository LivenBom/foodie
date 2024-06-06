import com.imooc.Application;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class StuServiceTest {
    @Autowired
    private StuService stuService;

    @Test
    public void myTest() {
       Stu stu =  stuService.getStuInfo("1211");
       System.out.println(stu.getName());
    }
}
