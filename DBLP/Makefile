#Variables:
csvPath = "SoTeSoLa2012.csv"

build:
	javac -Xlint -nowarn -d . -classpath .:binLibs/* src/dblp/*.java 

test:
	echo "this might take a few moments"
	java -classpath .:binLibs/* dblp.DBLP ${csvPath} "./SoTeSoLa.dot"

clean:
	rm -f dblp/*.class
	rm -rf ./dblp

