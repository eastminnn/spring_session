package com.dto;

public record UserReqDto(
        String username,
        String password,
        String nickname
) {}