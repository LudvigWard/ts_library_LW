package se.yrgo.libraryapp.services;

import org.mockito.Mock;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    private UserService userService;
    @Test
    @SuppressWarnings("deprecation")
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";
        final LoginInfo info = new LoginInfo(id, passwordHash);
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));
        userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    void registerSuccess() {
        String name = "testuser";
        String realname = "testuser";
        String password = "password";
        String hashedPassword = "password";
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        userService = new UserService(userDao, encoder);

        when(userDao.register(name, realname, hashedPassword)).thenReturn(true);

        boolean result = userService.register(name, realname, password);

        assertThat(result).isTrue();
    }


    @Test
    void registerFailure() {
        String name = "testuser";
        String realname = "testuser";
        String password = "password";
        String hashedPassword = "password";
        final PasswordEncoder encoder =
                org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        userService = new UserService(userDao, encoder);

        when(userDao.register(name, realname, hashedPassword)).thenReturn(false);

        boolean result = userService.register(name, realname, password);

        assertThat(result).isFalse();
    }

    @Test
    void isNameAvailable_nullName() {
        userService = new UserService(userDao, null);
        boolean result = userService.isNameAvailable(null);
        assertThat(result).isFalse();
    }

    @Test
    void isNameAvailable_nameTooShort() {
        userService = new UserService(userDao, null);
        boolean result = userService.isNameAvailable("ab"); // Mindre än 3 tecken
        assertThat(result).isFalse();
    }

    @Test
    void isNameAvailable_validNameAndAvailable() {
        userService = new UserService(userDao, null);
        String name = "validName";

        when(userDao.isNameAvailable(name)).thenReturn(true);

        boolean result = userService.isNameAvailable(name);

        assertThat(result).isTrue();
    }

    @Test
    void isNameAvailable_validNameAndNotAvailable() {
        userService = new UserService(userDao, null);
        String name = "validName";

        when(userDao.isNameAvailable(name)).thenReturn(false);

        boolean result = userService.isNameAvailable(name);

        assertThat(result).isFalse();
    }
}
