ARTIFACT=build/libs/commons.jar

jar:
	gradle shadowJar 

run: jar
	java -Xmx128m -Xms128m -jar $(ARTIFACT) 20000 
