package br.alysonsantos.com.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class User {

    @NonNull
    private String name;

    @NonNull
    private long expiration;

    @NonNull
    private boolean muted;
}
