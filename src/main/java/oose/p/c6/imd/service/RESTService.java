package oose.p.c6.imd.service;


import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.IUserDao;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class RESTService {
    @Inject
    private Librarian l;

    @GET
    public String hello(){
        return l.tested();
    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject jo){
        String email = jo.getString("email");
        if(l.verifyLogin(email, jo.getString("password"))){
            Token t = TokenManager.getInstance().createTokenForUser(l.getUserByEmail(email));
            return Response.status(200).entity(t.getTokenString()).build();
        }
        return Response.status(400).build();
    }

    @POST
    @Path("/quest/qr")
    public void scanQrCode(JsonObject jo){
        String qrCode = jo.getString("qrCode");
        User user = TokenManager.getInstance().getUserFromToken(jo.getString("token"));
		l.scanQrCode(user, qrCode);
    }

    @GET
    @Path("/quest/remove")
    public void removeQuestFromQuestLog(@QueryParam("entryID") int entryID, @QueryParam("token") String token) {
        l.removeQuestFromQuestLog(entryID, token);
    }
}