package csci310.annotation;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";
    private static final String SECRET = "secret-key-12345";

    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor() throws Exception {
        key = new SecretKeySpec(SECRET.getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return "";
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return "";
    }
}
