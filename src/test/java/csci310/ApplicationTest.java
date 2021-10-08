package csci310;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    public void applicationContextTest() {
        Application.main(new String[] {});
    }
}