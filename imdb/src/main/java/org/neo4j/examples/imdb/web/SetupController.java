package org.neo4j.examples.imdb.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class SetupController extends SimpleFormController
{
    private final SetupControllerDelegate delegate;

    public SetupController( final SetupControllerDelegate delegate )
    {
        super();
        this.delegate = delegate;
    }

    @Override
    protected ModelAndView onSubmit( final Object command ) throws ServletException
    {
        final Map<String,Object> model = new HashMap<String,Object>();
        delegate.getModel( command, model );
        return new ModelAndView( getSuccessView(), "model", model );
    }
}
