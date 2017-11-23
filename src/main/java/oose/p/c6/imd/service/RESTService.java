package oose.p.c6.imd.service;


import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.Replica;
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
    Librarian l;

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
    @Path("/shop/buy/{id}")
    public Response buyReplica(@PathParam("id") int replicaId, @QueryParam("token") String token) {
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null)
        {
            l.buyReplica(user, replicaId);
            return Response.status(201).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/shop")
    public Response getReplicas(@QueryParam("token") String token) {
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null)
        {
            List<Replica> replicas = l.getAvailableReplicas(user);
            return Response.status(200).entity(replicas).build();
        }
        return Response.status(403).build();
    }
}
