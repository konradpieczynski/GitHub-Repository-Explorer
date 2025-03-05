package org.github.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.github.domain.Branch;
import org.github.domain.Repository;
import org.github.mapper.GithubRepositoryMapper;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
public class GithubResourceTest {

    @InjectMock
    GithubRepositoryMapper repositoryMapper;

    @Inject
    GitHubResource gitHubResource;

    @Test
    void testGetUserRepositoriesSuccess() {
        // Given
        List<Repository> repositories;
        Repository repository1 = new Repository();
        repository1.setName("repo1");
        repository1.setOwner("username");
        repository1.setBranches(List.of(new Branch("branch1", "sha1")));
        Repository repository2 = new Repository();
        repository2.setName("repo2");
        repository2.setOwner("username");
        repository2.setBranches(List.of(new Branch("branch2", "sha2")));
        repositories = List.of(repository1, repository2);
        when(repositoryMapper.mapToRepositories("username"))
                .thenReturn(Uni.createFrom().item(repositories));

        // When
        Response response;
        List<?> entity;
        try (Response r = gitHubResource.getUserRepositories("username").await().indefinitely()) {
            response = r;
            entity = (List<?>) r.getEntity();
        }

        // Then
        assertEquals(200, response.getStatus());
        assertEquals(2, entity.size());

        Repository resultRepo1 = (Repository) entity.get(0);
        Repository resultRepo2 = (Repository) entity.get(1);

        assertEquals("repo1", resultRepo1.getName());
        assertEquals("username", resultRepo1.getOwner());
        assertEquals(1, resultRepo1.getBranches().size());
        assertEquals("branch1", resultRepo1.getBranches().getFirst().getName());
        assertEquals("sha1", resultRepo1.getBranches().getFirst().getSha());

        assertEquals("repo2", resultRepo2.getName());
        assertEquals("username", resultRepo2.getOwner());
        assertEquals(1, resultRepo2.getBranches().size());
        assertEquals("branch2", resultRepo2.getBranches().getFirst().getName());
        assertEquals("sha2", resultRepo2.getBranches().getFirst().getSha());
    }
}