package com.example.sber.repos;

import com.example.sber.domain.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User user1 = new User("login", "name", "surname", "addres");
    private User user2 = new User("login2", "name2", "surname", "addres");

    @Before
    public void preparation ()
    {
        Iterable<User> users = userRepo.findAll();
        userRepo.save(user1);
        userRepo.save(user2);
    }

    @After
    public void clear()
    {
        userRepo.deleteAll();
    }

    @Test
    public void testDelete()
    {
        userRepo.delete(user1);
        Assert.assertEquals(userRepo.count(), 1);
        Assert.assertNotNull(userRepo.findFirstById(user2.getId()));
        Assert.assertNull(userRepo.findFirstById(user1.getId()));
    }

}