package org.neo4j.examples.imdb.parser;

import java.util.List;

import org.neo4j.examples.imdb.domain.Actor;
import org.neo4j.examples.imdb.domain.ImdbService;
import org.neo4j.examples.imdb.domain.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class ImdbReaderImpl implements ImdbReader
{
    @Autowired
    private ImdbService imdbService;

    @Transactional
    public void newActors( final List<ActorData> actorList )
    {
        for ( ActorData actorData : actorList )
        {
            newActor( actorData.getName(), actorData.getMovieRoles() );
        }
    }

    @Transactional
    public void newMovies( final List<MovieData> movieList )
    {
        for ( MovieData movieData : movieList )
        {
            newMovie( movieData.getTitle(), movieData.getYear() );
        }
    }

    private void newMovie( final String title, final int year )
    {
        imdbService.createMovie( title, year );
    }

    private void newActor( final String name, final RoleData[] movieRoles )
    {
        final Actor actor = imdbService.createActor( name );
        for ( RoleData movieRole : movieRoles )
        {
            final Movie movie = imdbService
                .getExactMovie( movieRole.getTitle() );
            if ( movie != null )
            {
                imdbService.createRole( actor, movie, movieRole.getRole() );
            }
        }
    }
}
