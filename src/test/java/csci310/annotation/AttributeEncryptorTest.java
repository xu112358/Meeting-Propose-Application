package csci310.annotation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AttributeEncryptorTest {

    AttributeEncryptor attributeEncryptor;

    @Before
    public void setUp() throws Exception {
        attributeEncryptor = new AttributeEncryptor();
    }

    @Test
    public void testConvertToDatabaseColumn() {
        assertEquals("YJIoHUi9jpgngZ+GyiApyQ==", attributeEncryptor.convertToDatabaseColumn(new String()));
    }

    @Test
    public void testConvertToEntityAttribute() {
        assertEquals("", attributeEncryptor.convertToEntityAttribute(new String()));
    }
}