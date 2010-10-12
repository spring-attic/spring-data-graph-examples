sed -e 's/@RooToString/\/\/@RooToString/' -e '/}/ i\ 
\
\    public String toString() { \
\       return getName(); \
\    }
'
