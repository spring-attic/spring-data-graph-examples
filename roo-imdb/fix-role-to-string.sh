sed -e 's/@RooToString/\/\/@RooToString/' -e '/}/ i\ 
\
\    public String toString() { \
\       return String.format("%s as %s in %s", getActor(), getRole(), getMovie()); \
\    }
'
