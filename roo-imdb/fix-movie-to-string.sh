sed -e 's/@RooToString/\/\/@RooToString/' -e '/}/ i\ 
\
\    public String toString() { \
\       return String.format("%s (%d)", getTitle(), getYear()); \
\    }
'
