
mvn clean package exec:java -Dexec.mainClass="ibm.cte.mq.jms.JMSItemProducer"  -Dexec.args="src/main/resources/config-ioc.properties data/item1.json"
