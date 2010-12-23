package org.neo4j.examples.imdb.web;

import java.util.Map;

import javax.servlet.ServletException;

public interface FindControllerDelegate
{
    void getModel( Object command, Map<String,Object> model ) throws ServletException;

    String getFieldName();
}