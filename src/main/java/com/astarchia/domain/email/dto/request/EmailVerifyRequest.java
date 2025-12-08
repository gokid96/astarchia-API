package com.astarchia.domain.email.dto.request;

public record EmailVerifyRequest(String email, String code) {}