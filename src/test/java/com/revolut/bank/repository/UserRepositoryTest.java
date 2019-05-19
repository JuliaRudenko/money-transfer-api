package com.revolut.bank.repository;

import com.revolut.bank.exception.NoSuchEntityException;
import com.revolut.bank.model.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserRepositoryTest {
    private static UserRepository userRepository;

    @BeforeClass
    public static void init() throws SQLException {
        userRepository = new UserRepository();
    }

    @Before
    public void refreshDbData() throws SQLException {
        userRepository.fillDbWithData();
    }

    @Test
    public void checkFindAllUsers() throws SQLException {
        List<User> users = userRepository.findAllUsers();
        assertEquals(3, users.size());
    }

    @Test
    public void checkFindUserById() throws SQLException {
        User user = userRepository.findUserById(1L);
        assertEquals("Eddard Stark", user.getUserName());
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkFindNonExistentUserById() throws SQLException {
        userRepository.findUserById(10L);
    }

    @Test
    public void checkCreateUser() throws SQLException {
        User user = new User("Daenerys Targaryen", "dany@gmail.com");
        Long id = userRepository.createUser(user);
        assertEquals(new Long(4L), id);
    }

    @Test
    public void checkUpdateUserNameAndEmail() throws SQLException {
        String userName = "Robb Stark";
        String email = "robb@gmail.com";
        User user = new User(1L, userName, email);

        userRepository.updateUser(user);
        // already tested by another tests
        User userFromDb = userRepository.findUserById(1L);

        assertEquals(userName, userFromDb.getUserName());
        assertEquals(email, userFromDb.getEmail());
    }

    @Test
    public void checkUpdateUserName() throws SQLException {
        String userName = "Robb Stark";
        String email = "eddard@gmail.com";
        User user = new User();
        user.setId(1L);
        user.setUserName(userName);

        userRepository.updateUser(user);
        // already tested by another tests
        User userFromDb = userRepository.findUserById(1L);

        assertEquals(userName, userFromDb.getUserName());
        assertEquals(email, userFromDb.getEmail());
    }

    @Test
    public void checkUpdateEmail() throws SQLException {
        String userName = "Eddard Stark";
        String email = "robb@gmail.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        userRepository.updateUser(user);
        // already tested by another tests
        User userFromDb = userRepository.findUserById(1L);

        assertEquals(userName, userFromDb.getUserName());
        assertEquals(email, userFromDb.getEmail());
    }

    @Test
    public void checkWillNotUpdateCreatedAt() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date updateDate = format.parse("2020-10-10 11:00:00");
        Date expectedDate = format.parse("2019-05-01 17:00:00");
        User user = new User();
        user.setId(1L);
        user.setCreatedAt(updateDate);

        userRepository.updateUser(user);
        // already tested by another tests
        User userFromDb = userRepository.findUserById(1L);

        assertEquals(expectedDate, userFromDb.getCreatedAt());
    }

    @Test(expected = NoSuchEntityException.class)
    public void checkThatNoUserAfterDelete() throws SQLException {
        userRepository.deleteUser(3L);
        userRepository.findUserById(3L);
    }

    @Test
    public void checkThatTwoUsersLeftAfterDelete() throws SQLException {
        userRepository.deleteUser(3L);
        List<User> users = userRepository.findAllUsers();
        assertEquals(2, users.size());
    }
}
