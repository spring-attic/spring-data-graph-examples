This project contains scripts for the Roo PetClinic Graph Demo

Copy these scripts to a base directory.

Next set the $ROO_HOME environment variable:

  export ROO_HOME=~/springsource/spring-roo-1.1.0.RC1

Then create the directory for the demo app:

  > mkdir petclinic
  > cd petclinic

Now we can start Roo:

  > roo.sh

Inside Roo

  roo> script ../petclinic-script
  ...
  roo> exit

Then run ../fix-petclinic script 

At last you can now do:

  > mvn tomcat:run


