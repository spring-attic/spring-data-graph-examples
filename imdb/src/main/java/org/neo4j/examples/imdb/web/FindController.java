package org.neo4j.examples.imdb.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class FindController extends SimpleFormController
{
    private final FindControllerDelegate delegate;

    public FindController( final FindControllerDelegate delegate )
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

    @Override
    protected boolean isFormSubmission( final HttpServletRequest request )
    {
        final String field = request.getParameter( delegate.getFieldName() );
        return field != null && field.trim().length() > 0;
    }
}
