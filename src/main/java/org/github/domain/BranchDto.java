package org.github.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BranchDto {
    private String name;
    private CommitDto commit;
}