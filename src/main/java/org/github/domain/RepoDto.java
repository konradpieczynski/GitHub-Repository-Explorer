package org.github.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RepoDto {
    private String name;
    private OwnerDto owner;
    private boolean fork;
}
