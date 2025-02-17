package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Disabled
public class PasswordEncodingTests {

    static final String PASSWORD = "password";

    //What happens if you use a higher value for strength?
    //
    // The computational cost becomes exponentially higher,
    // thus taking longer to calculate hash.
    // This can be used to deter brute force attacks by reducing the number of calculations
    // which can be performed in a given time span.
    @Test
    void testBCrypt15() {
        PasswordEncoder bcrypt15 = new BCryptPasswordEncoder(15);
        System.out.println(bcrypt15.encode("tiger"));
    }

    @Test
    void testBCrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode("guru"));
    }

    @Test
    void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode("password"));
    }

    @Test
    //this algorithm is using a random salt
    void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode("tiger"));

        String encodedPassword = ldap.encode(PASSWORD);

        Assertions.assertTrue(ldap.matches(PASSWORD, encodedPassword));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();

        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    //It is not recommended for password use
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySALTVALUE";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }
}
