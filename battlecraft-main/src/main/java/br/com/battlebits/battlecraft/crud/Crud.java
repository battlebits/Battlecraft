package br.com.battlebits.battlecraft.crud;

import java.util.Set;

public interface Crud<T> {

    void create(T model);

    void remove(T model);

    T get(T model);

    Set<T> all();

}
