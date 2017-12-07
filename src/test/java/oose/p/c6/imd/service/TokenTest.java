package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.service.*;
import org.junit.Test;
import org.mockito.Mock;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;


public class TokenTest {
    @Test
    public void createTokenForUser(){
        User u = new User(1, "a","a","a", 100, 1);
        Token t = TokenManager.getInstance().createTokenForUser(u);
        assertEquals(u, t.getUser());
    }

    @Test
    public void getNullUserFromToken(){
        assertNull(TokenManager.getInstance()
                .getUserFromToken("a"));
    }

    @Test
    public void getTokenFromTokenString(){
        User u = new User(1, "a","a","a", 100, 1);
        Token t = TokenManager.getInstance().createTokenForUser(u);
        assertEquals(TokenManager.getInstance().getTokenFromTokenString(t.getTokenString()), t);
    }

    @Test
    public void getNullTokenFromTokenString(){
        assertNull(TokenManager.getInstance().getTokenFromTokenString("a"));
    }


}
