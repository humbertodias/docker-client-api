package com.docker.client.demo;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class DockerClientService {

    DockerClient dockerClient;

    @PostConstruct
    public void setup() {
        dockerClient = DockerClientBuilder.getInstance().build();
    }

    public List<Container> containers() {
        return dockerClient.listContainersCmd()
                .withShowSize(true)
                .withShowAll(true)
                .exec();
    }

    public List<Image> images() {
        return dockerClient.listImagesCmd().exec();
    }

    public InspectImageResponse inspectImage(String imageId) {
        return dockerClient.inspectImageCmd(imageId).exec();
    }

    public InspectContainerResponse inspectContainer(String containerId) {
        return dockerClient.inspectContainerCmd(containerId).exec();
    }

    public InspectVolumeResponse inspectVolume(String id) {
        return dockerClient.inspectVolumeCmd(id).exec();
    }

    public boolean pull(String image, String tag) throws InterruptedException {
        return dockerClient.pullImageCmd(image)
                .withTag(tag)
                .exec(new PullImageResultCallback())
                .awaitCompletion(30, TimeUnit.SECONDS);
    }

    public List<InspectVolumeResponse> volumes() {
        ListVolumesResponse volumesResponse = dockerClient.listVolumesCmd().exec();
        return volumesResponse.getVolumes();
    }

    public CreateContainerResponse create(String image, String[] cmds) {
        return dockerClient.createContainerCmd(image)
                .withCmd(cmds)
                .exec();
    }

    public Void start(String containerId){
        return dockerClient.startContainerCmd(containerId).exec();
    }

    public String exec(String containerId, String cmd) throws InterruptedException, UnsupportedEncodingException {
        InputStream stdin = new ByteArrayInputStream(cmd.getBytes("UTF-8"));
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();

        boolean completed = dockerClient.execStartCmd(containerId)
                .withDetach(false)
                .withTty(true)
                .withStdIn(stdin)
                .exec(new ExecStartResultCallback(stdout, System.err))
                .awaitCompletion(5, TimeUnit.SECONDS);

        return String.valueOf(stdout);
    }

}