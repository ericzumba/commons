ARTIFACT=build/libs/commons.jar

jar:
	gradle build

run: jar
	java -jar $(ARTIFACT) java.util.concurrent.ConcurrentHashMap	1000000
