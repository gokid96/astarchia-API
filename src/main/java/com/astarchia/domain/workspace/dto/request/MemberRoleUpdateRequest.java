package com.astarchia.domain.workspace.dto.request;

import com.astarchia.domain.workspace.entity.WorkspaceRole;
import jakarta.validation.constraints.NotNull;

public record MemberRoleUpdateRequest(
    @NotNull WorkspaceRole role
) {}