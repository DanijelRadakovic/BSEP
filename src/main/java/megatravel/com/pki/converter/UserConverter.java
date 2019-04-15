package megatravel.com.pki.converter;

import megatravel.com.pki.domain.DTO.PrivilegeDTO;
import megatravel.com.pki.domain.DTO.RegisteringDTO;
import megatravel.com.pki.domain.DTO.RoleDTO;
import megatravel.com.pki.domain.DTO.UserDTO;
import megatravel.com.pki.domain.Privilege;
import megatravel.com.pki.domain.Role;
import megatravel.com.pki.domain.User;
import megatravel.com.pki.domain.enums.UserType;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UserConverter extends AbstractConverter {

    public static User toRegisteringEntity(RegisteringDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        byte[] salt = generateSalt();
        user.setSalt(salt);
        user.setPassword(hashPassword(dto.getPassword(), salt));
        return user;
    }

    public static UserDTO fromRegisteringEntity(User entity){
        UserDTO dto = new UserDTO();
        dto.setUsername(entity.getUsername());
        Set<RoleDTO> roles = new HashSet<>();
        for(Role r : entity.getRoles()){
            RoleDTO role = new RoleDTO();
            role.setId(r.getId());
            role.setName(r.getName());
            Set<PrivilegeDTO> privileges = new HashSet<>();
            for(Privilege p : r.getPrivileges()){
                PrivilegeDTO privilege = new PrivilegeDTO();
                privilege.setId(p.getId());
                privilege.setName(p.getName());
                privileges.add(privilege);
            }
            role.setPrivileges(privileges);
            roles.add(role);
        }
        dto.setRoles(roles);
        return dto;
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
