package org.github.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Repository {
    private String name;
    private String owner;
    private List<Branch> branches;
}
