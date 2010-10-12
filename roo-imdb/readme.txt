This project contains scripts for the Roo IMDB Demo

Copy these scripts to a base directory.

Next set the $ROO_HOME environment variable:

  export ROO_HOME=~/Projects/springsource/git/roo/roo

Then create the directory for the demo app:

  > mkdir imdb
  > cd imdb

Now we can start Roo:

  > ../run-roo-demo

Inside Roo

  roo> script ../imdb-script
  ...
  roo> exit

The ../fix script will automatically run so you can now do:

  > mvn tomcat:run


