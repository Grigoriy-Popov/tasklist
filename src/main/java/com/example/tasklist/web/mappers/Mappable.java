package com.example.tasklist.web.mappers;

import java.util.Collection;

public interface Mappable<E, D> {

    D toDto(E entity);

    Collection<D> toDto(Collection<E> entities);

    E toEntity(D dto);

}
