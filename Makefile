ARTIFACT=build/libs/commons.jar

jar:
	gradle build

run: jar
	java -Xmx256m -Xms256m -jar $(ARTIFACT) java.util.concurrent.ConcurrentHashMap	10
