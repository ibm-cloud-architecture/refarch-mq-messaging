mvn clean package exec:java -Dexec.mainClass="ibm.cte.mq.jms.JMSItemProducer"  -Dexec.cleanupDaemonThreads=false -Dexec.args="data/item1.json"
