package org.github.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.github.domain.BranchDto;
import org.github.domain.RepoDto;

import java.util.List;

@RegisterRestClient(baseUri = "https://api.github.com")
@ApplicationScoped
public interface GitHubService {
    @GET
    @Path("/users/{username}/repos")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<List<RepoDto>> getUserRepositories(@PathParam("username") String username);

    @GET
    @Path("/repos/{username}/{repositoryName}/branches")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<List<BranchDto>> getBranches(@PathParam("username") String username, @PathParam("repositoryName") String repositoryName);
}