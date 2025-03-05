package org.github.mapper;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.github.domain.Branch;
import org.github.domain.Repository;
import org.github.domain.RepoDto;
import org.github.service.GitHubService;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GithubRepositoryMapper {
    @Inject
    @RestClient
    GitHubService gitHubService;

    @Inject
    GithubBranchMapper branchMapper;

    public Uni<List<Repository>> mapToRepositories(String username) {
        return gitHubService.getUserRepositories(username)
                .onItem().transformToUni(repoDtos -> {
                    List<RepoDto> nonForkedRepos = repoDtos.stream()
                            .filter(repo -> !repo.isFork())
                            .toList();
                    if (nonForkedRepos.isEmpty()) {
                        return Uni.createFrom().item(List.of());
                    }

                    List<Uni<Repository>> repositoryUnis = nonForkedRepos.stream()
                            .map(repo -> mapRepositoryWithBranches(repo.getOwner().getLogin(), repo))
                            .toList();
                    return Uni.join().all(repositoryUnis).andFailFast();
                });
    }

    private Uni<Repository> mapRepositoryWithBranches(String username, RepoDto repoDto) {
        return gitHubService.getBranches(username, repoDto.getName())
                .onItem().transform(branchDtos -> {
                    List<Branch> branches = branchDtos.stream()
                            .map(branchMapper::mapToBranch)
                            .collect(Collectors.toList());

                    Repository repository = new Repository();
                    repository.setName(repoDto.getName());
                    repository.setOwner(repoDto.getOwner().getLogin());
                    repository.setBranches(branches);

                    return repository;
                });
    }
}