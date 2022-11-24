rm MiniCAD.jar
javac -encoding utf8 src/*.java
cd src
jar -cvfe  ../MiniCAD.jar Main *.class
rm *.class
cd ..
java -jar  MiniCAD.jar