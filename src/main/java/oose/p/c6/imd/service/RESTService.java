package oose.p.c6.imd.service;


import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.Replica;
import oose.p.c6.imd.domain.User;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyReplica(@PathParam("id") int replicaId, @QueryParam("token") String token) {
        //User user = TokenManager.getInstance().getUserFromToken(token);
        User user = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 600, 1);
        if(user != null)
        {
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonObjectBuilder job = factory.createObjectBuilder();
            boolean isPurchased = l.buyReplica(user, replicaId);
            System.out.println(isPurchased);
            job.add("isPurchased", isPurchased);

            return Response.status(201).entity(job.build()).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/shop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReplicas(@QueryParam("token") String token) {
        //User user = TokenManager.getInstance().getUserFromToken(token);
        User user = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 1000, 1);
        if(user != null)
        {
            List<Replica> replicas = l.getAvailableReplicas(user);
            return Response.status(200).entity(replicas).build();
        }
        return Response.status(403).build();
    }
}
