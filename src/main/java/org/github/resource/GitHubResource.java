package org.github.resource;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.github.domain.ErrorResponse;
import org.github.mapper.GithubRepositoryMapper;

@Path("/github")
@ApplicationScoped
public class GitHubResource {
    @Inject
    GithubRepositoryMapper repositoryMapper;

    @GET
    @Path("/{username}/repos")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getUserRepositories(@PathParam("username") String username) {
        return repositoryMapper.mapToRepositories(username)
                .onItem().transform(repositories -> Response.ok(repositories).build())
                .onFailure().recoverWithItem(e -> {
                    if (e instanceof WebApplicationException webEx) {
                        if (webEx.getResponse().getStatus() == 404) {
                            return Response.status(404)
                                    .entity(new ErrorResponse(404, "User not found"))
                                    .build();
                        }
                    }
                    return Response.serverError().build();
                });
    }
}