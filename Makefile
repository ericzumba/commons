ARTIFACT=build/libs/commons.jar

jar:
	gradle shadowJar 

run: jar
	java -Xmx256m -Xms256m -jar $(ARTIFACT) java.util.concurrent.ConcurrentHashMap	1000000
