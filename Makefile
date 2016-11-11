ARTIFACT=build/libs/commons.jar

jar:
	gradle shadowJar 

run: jar
	java -Xmx128m -Xms128m -jar $(ARTIFACT) java.util.concurrent.ConcurrentHashMap 2000000 
