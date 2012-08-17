build:
	javac -Xlint -nowarn -d . -classpath .:binLibs/* src/dblp/*.java 

test:
	echo "this might take a few moments"
	java -classpath .:binLibs/* dblp.DBLP "Ralf Lämmel"

clean:
	rm -f dblp/*.class
	rm -rf ./dblp

