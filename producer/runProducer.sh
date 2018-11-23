mvn clean package exec:java -Dexec.mainClass="ibm.cte.mq.poc.ProduceItemMessage"  -Dexec.args="src/main/resources/config.properties data/item1.json"
