package com.demo.catalog.management.service;

/**
 * This interface has all the CRUD operations
 */

public interface CatalogService<Subject> {

    void add(Subject subject);

    void update(String subjectName, String optionalSubjectName, Subject subject);

    void remove(String subjectName, String optionalSubjectName);

    Subject get(String subjectName, String optionalSubjectName);

}
