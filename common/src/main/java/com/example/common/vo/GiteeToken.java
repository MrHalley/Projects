package com.example.common.vo;

import lombok.Data;

@Data
public class GiteeToken {

    private String access_token;

    private String token_type;

    private long expires_in;

    private String refresh_token;

    private long created_at;

}
