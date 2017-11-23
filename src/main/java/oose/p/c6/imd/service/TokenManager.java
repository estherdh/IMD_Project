package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.User;

import javax.ejb.Singleton;
import java.time.Instant;
import java.util.ArrayList;

public class TokenManager {
    private static TokenManager instance;
    private ArrayList<Token> tokens = new ArrayList<>();

	public static TokenManager getInstance(){
	    if(instance != null){
            return instance;
        } else {
	        instance = new TokenManager();
	        return instance;
        }
	}

    private TokenManager() {

    }

    public Token createTokenForUser(User u){
	    Token t = new Token(u, Instant.now().getEpochSecond());
	    tokens.add(t);
	    return t;
    }

    public User getUserFromToken(String t){
        for(Token token : tokens){
            if(token.tokenStringCorrect(t)){
                return token.getUser();
            }
        }
        return null;
    }

    public static void setInstance(TokenManager newInstance) {
    	instance = newInstance;
	}


}
