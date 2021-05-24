package com.docker.client.demo;

import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/docker")
@Singleton
@Produces(APPLICATION_JSON)
public class DockerController {

    @Inject
    DockerClientService client;

    @GET
    @Path("containers")
    public List<Container> containers(){
        return client.containers();
    }

    @GET
    @Path("images")
    public List<Image> images(){
        return client.images();
    }

    @GET
    @Path("inspect/image/{id}")
    public InspectImageResponse image(@PathParam("id") String id){
        return client.inspectImage(id);
    }

    @GET
    @Path("inspect/container/{id}")
    public InspectContainerResponse container(@PathParam("id") String id){
        return client.inspectContainer(id);
    }

    @GET
    @Path("inspect/volume/{id}")
    public InspectVolumeResponse volume(@PathParam("id") String id){
        return client.inspectVolume(id);
    }

    @GET
    @Path("volumes")
    public List<InspectVolumeResponse> volumes(){
        return client.volumes();
    }

    @GET
    @Path("pull/{image}/{tag}?")
    public boolean pull(@PathParam("image") String image,@PathParam("tag") @DefaultValue("latest") String tag) throws InterruptedException {
        return client.pull(image, tag);
    }

    @GET
    @Path("create/{image}/{cmds}")
    public CreateContainerResponse create(@PathParam("image") String image, @PathParam("cmds") String cmds) {
        return client.create(image, cmds.split(","));
    }

    @GET
    @Path("start/{id}")
    public Void start(@PathParam("id") String id)  {
        return client.start(id);
    }

    @GET
    @Path("exec/{containerId}/{cmds}")
    public String exec(@PathParam("containerId") String containerId, @PathParam("cmds") String cmd) throws InterruptedException, UnsupportedEncodingException {
        return client.exec(containerId, cmd.replaceAll(","," "));
    }

}
