package oose.p.c6.imd.service;


import oose.p.c6.imd.domain.*;

import javax.inject.Inject;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/")
public class RESTService {
    private static final Logger LOGGER = Logger.getLogger(RESTService.class.getName());

    @Inject
    private Librarian l;

    @GET
    @Path("/")
    public String hello(){
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("email", "test@void");
        job.add("password", "test");
        JsonObject jo = job.build();
        String token = ((JsonObject) login(jo).getEntity()).getString("token");
        TokenManager.getInstance().getTokenFromTokenString(token).devSetTokenString();
        return "hello world";
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject jo){
        String email = jo.getString("email");
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        int loginState = l.verifyLogin(email, jo.getString("password"));
        if(loginState == 0){
            Token t = TokenManager.getInstance().createTokenForUser(l.getUserByEmail(email));
            job.add("token", t.getTokenString());
            return Response.status(201).entity(job.build()).build();
        } else {
            job.add("reason", loginState );
            return Response.status(401).entity(job.build()).build();
        }
    }

    @POST
    @Path("/shop/buy/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyReplica(@PathParam("id") int replicaId, @QueryParam("token") String token) {
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null)
        {
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonObjectBuilder job = factory.createObjectBuilder();
            boolean isPurchased = l.buyReplica(user, replicaId);
            job.add("isPurchased", isPurchased);

            return Response.status(201).entity(job.build()).build();
        }
        return Response.status(403).build();
    }

    @GET
    @Path("/shop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReplicas(@QueryParam("token") String token) {
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null)
        {
            List<Replica> replicas = l.getAvailableReplicas(user);
            return Response.status(200).entity(replicas).build();
        }
        return Response.status(403).build();
    }

    @POST
    @Path("/quest/qr")
    @Consumes(MediaType.APPLICATION_JSON)
    public void scanQrCode(@QueryParam("token") String token, JsonObject jo){
        String qrCode = jo.getString("qrCode");
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null){
            l.scanQrCode(user, qrCode);
        }
    }

    @GET
    @Path("/quest/remove")
    public void removeQuestFromQuestLog(@QueryParam("entryID") int entryID, @QueryParam("token") String token) {
        User user = TokenManager.getInstance().getUserFromToken(token);
        l.removeQuestFromQuestLog(entryID, user);
    }

    @GET
    @Path("/exhibit/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExhibitDetails(@PathParam("id") int exhibitId, @QueryParam("token") String token){
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null){
            Exhibit e = l.getExhibitDetails(user, exhibitId);
            if(e != null) {
                return Response.status(200).entity(buildExhibitJson(e)).build();
            }
            return Response.status(200).build();
        }
        return Response.status(401).build();
    }

    private JsonObject buildExhibitJson(Exhibit e){
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("ExhibitId", e.getId());
        job.add("Name", e.getName());

        String image = e.getImage();
        if(image == null){
            image = "undefined";
        }
        String video = e.getVideo();
        if(video == null){
            video = "undefined";
        }
        job.add("Description", e.getDescription());
        job.add("Video", video);
        job.add("Image", image);
        job.add("Year", e.getYear());
        return job.build();
    }

    @GET
    @Path("/museum/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findMuseum(@PathParam("id") int museumId, @QueryParam("token") String token){
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null){
            Museum m = l.findMuseum(museumId);
            if(m != null) {
                return Response.status(200).entity(createMusuemJson(m)).build();
            }
            return Response.status(200).build();
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/era/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findEra(@PathParam("id") int eraId, @QueryParam("token") String token){
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null){
            Era e = l.findEra(user, eraId);
            if(e != null) {
                return Response.status(200).entity(createEraJson(e)).build();
            }
            return Response.status(200).build();
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/museum")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMuseums(@QueryParam("token") String token){
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null){
            List<Museum> list = l.listMuseums();
            if(list.size() > 0) {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);
                JsonArrayBuilder jab = factory.createArrayBuilder();
                for (Museum m: list) {
                    jab.add(createMusuemJson(m));
                }
                return Response.status(200).entity(jab.build()).build();
            }
            return Response.status(200).build();
        }
        return Response.status(401).build();
    }

    @GET
    @Path("/era")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listEra(@QueryParam("token") String token){
        User user = TokenManager.getInstance().getUserFromToken(token);
        if(user != null){
            List<Era> list = l.listEra(user);

            if(list.size() > 0) {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);
                JsonArrayBuilder jab = factory.createArrayBuilder();
                for (Era e: list) {
                    jab.add(createEraJson(e));
                }
                return Response.status(200).entity(jab.build()).build();
            }
            return Response.status(200).build();
        }
        return Response.status(401).build();
    }

    private JsonObject createEraJson(Era e){
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("EraId", e.getId());
        job.add("Name", e.getName());
        return job.build();
    }

    private JsonObject createMusuemJson(Museum m){
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("MuseumId", m.getId());
        job.add("Name", m.getName());
        job.add("Site", m.getSite());
        job.add("Region", m.getRegion());
        return job.build();
    }
}