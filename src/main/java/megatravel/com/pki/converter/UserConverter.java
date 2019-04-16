package megatravel.com.pki.converter;

import megatravel.com.pki.domain.Admin;
import megatravel.com.pki.domain.DTO.RegisteringDTO;
import megatravel.com.pki.domain.Operator;
import megatravel.com.pki.domain.enums.UserType;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class UserConverter extends AbstractConverter {

    public static User toRegisteringEntity(RegisteringDTO dto){
        User user;
        if(dto.getType() == UserType.ADMIN){
            user = new Admin();
        }
        else{
            user = new Operator();
        }
        user.setUsername(dto.getUsername());
        byte[] salt = generateSalt();
        user.setSalt(salt);
        user.setPassword(hashPassword(dto.getPassword(), salt));
        return user;
    }

    /**
     * generating salt for storing in database
     */
    private static byte[] generateSalt(){
        Random random = new SecureRandom(ByteBuffer.allocate(4)
                .putInt(LocalDateTime.now().getSecond()).array());
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashing password
     * @param password - inserted password
     * @param salt - hehe :D
     * @return hashed password
     */
    private static byte[] hashPassword(String password, byte[] salt){
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 255);
        Arrays.fill(password.toCharArray(), Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

}
