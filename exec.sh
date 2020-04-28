mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt
CLASSPATH=`cat classpath.txt`

java -cp \
"$CLASSPATH"\
org.testng.TestNG \
src/test/java/GoRest.java
