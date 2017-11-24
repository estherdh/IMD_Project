package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.User;

public class Token {
    private String string;
    private User u;
    private Long timeCreated;
    Token(User user, long timeCreated) {
        this.u = user;
        this.timeCreated = timeCreated;
        this.string = generateRandomTokenSting();
    }

    private String generateRandomTokenSting(){
        return new RandomTokenString().nextString();
    }

    public boolean tokenStringCorrect(String t){
        return t.equals(string);
    }

    public User getUser(){
        return u;
    }

    public String getTokenString(){
        return string;
    }

    public void DeVsetTokenString(){
        this.string = "1234-1234-1234";
    }
}
