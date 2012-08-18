testall: build test

build: 
	javac -Xlint -nowarn -d . -classpath .:binLibs/* src/dbpedia/*.java 

test:
	@echo "combining some 101Wiki and DBPedia information about languages - see output.json for result" 
	java -classpath .:binLibs/* dbpedia.DBPedia "Wiki101Full.json" "output.json"

clean:
	rm -f dbpedia/*.class
	rm -rf ./dbpedia
	rm -f output.json

