%
% Install Xeger on local repository
% ---------------------------------
% 
% Put xeger-1.0-SNAPSHOT.jar in the current directory and run this script.
%
%
mvn install:install-file -DgroupId=nl.flotsam -DartifactId=xeger -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dfile=xeger-1.0-SNAPSHOT.jar -DgeneratePom=true