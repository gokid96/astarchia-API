package com.astarchia.domain.workspace.dto.request;

public record WorkspaceUpdateRequest(
    String name,
    String description
) {}