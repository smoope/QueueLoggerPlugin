# QueueLoggerPlugin

Simple ActiveMQ plugin for logging events based on queue names.

## Installation

1. Copy jar file to `ActiveMQ/lib` folder
2. Add plugin definition to activemq.xml:

```
<broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}">
        <plugins>
            <bean id="queueLoggerPlugin"
                  class="com.smoope.utils.activemq.QueueLoggerPlugin"
                  xmlns="http://www.springframework.org/schema/beans">
                <property name="destinations">
                    <map>
                        <entry key="ActiveMQ.DLQ" value="warn"/>
                        <entry key="some-queue-name" value="debug"/>
                    </map>
                </property>
            </bean>
        </plugins>
   ...
</broker>
```
