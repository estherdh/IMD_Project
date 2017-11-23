package oose.p.c6.imd.service;


import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.IUserDao;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class RESTService {
    @Inject
    private Librarian l;


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject jo){
        String email = jo.getString("email");
        if(l.verifyLogin(email, jo.getString("password"))){
            Token t = TokenManager.getInstance().createTokenForUser(l.getUserByEmail(email));
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonObjectBuilder job = factory.createObjectBuilder();
            job.add("token", t.getTokenString());
            return Response.status(201).entity(job.build()).build();
        }
        return Response.status(401).build();
    }

    @POST
    @Path("/quest/qr")
    public void scanQrCode(@QueryParam("token") String token, JsonObject jo){
        String qrCode = jo.getString("qrCode");
        User user = TokenManager.getInstance().getUserFromToken(token);
		l.scanQrCode(user, qrCode);
    }
}