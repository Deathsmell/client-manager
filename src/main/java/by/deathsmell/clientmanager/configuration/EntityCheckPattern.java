package by.deathsmell.clientmanager.configuration;

import java.util.Set;

public interface EntityCheckPattern<T> {
    boolean check(T entity);
    void fix(T entity, String correct);
    String getFieldType(T entity);
    Integer getEntityId(T entity);
    String[] getValidTypes();
    void getInvalidType(Set<String> source, T entity);
}