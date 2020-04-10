package br.alysonsantos.com.core.repository;

import java.util.Optional;

public interface Repository<String, R> {
    Optional<R> find(String id);

    void insert(String id, R r);

    void update(String id, R r);

    void delete(String id);
}
