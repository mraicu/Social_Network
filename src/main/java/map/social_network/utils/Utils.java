package map.social_network.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
public class Utils {
    public java.sql.Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date utilDate = Date.from(instant);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }

    public LocalDateTime dateToLocalDateTime(Date date) {

        Instant instant = Instant.ofEpochMilli(date.getTime());

        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime;
    }

    public LocalDateTime stringToLocalDateTime(String month) {
        month = "2000-" + month + "-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth yearMonth = YearMonth.parse(month, formatter);

        return yearMonth.atDay(1).atStartOfDay();
    }

    public static String encryptPassword(String password, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");

        SecretKeySpec keySpec = generateKey(secretKey);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encrypted = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // generate a secret key from a given string
    private static SecretKeySpec generateKey(String secretKey) throws Exception {
        //Password-Based Key Derivation Function 2 with HmacSHA256
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), "salt".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    // decrypt an encrypted password
    public static String decryptPassword(String encryptedPassword, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");

        SecretKeySpec keySpec = generateKey(secretKey);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
        byte[] decrypted = cipher.doFinal(decoded);

        return new String(decrypted);
    }



}
