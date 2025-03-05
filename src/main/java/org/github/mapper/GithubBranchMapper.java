package org.github.mapper;

import org.github.domain.Branch;
import org.github.domain.BranchDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GithubBranchMapper {
    Branch mapToBranch(BranchDto branchDto) {
        return new Branch(branchDto.getName(), branchDto.getCommit().getSha());
    }
}
