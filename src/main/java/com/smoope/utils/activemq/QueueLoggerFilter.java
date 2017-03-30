/*
 * Copyright 2017 smoope GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smoope.utils.activemq;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.command.Message;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class QueueLoggerFilter extends BrokerFilter {

    private final static Logger LOG = Logger.getLogger(QueueLoggerFilter.class);

    private Map<String, String> params;

    public QueueLoggerFilter(final Broker next, Map<String, String> params) {
        super(next);

        this.params = params;
    }

    @Override
    public void send(final ProducerBrokerExchange producerExchange, final Message messageSend) throws Exception {
        final String destination = messageSend.getDestination().getPhysicalName();
        config(destination).ifPresent(level ->
            log(level, String.format("Sent to %s: %s", destination, messageSend)));

        super.send(producerExchange, messageSend);
    }

    @Override
    public void sendToDeadLetterQueue(final ConnectionContext context, final MessageReference messageReference) {
        final String destination = "ActiveMQ.DLQ";
        config(destination).ifPresent(level ->
            log(level, String.format("Sent to %s: %s", destination, messageReference)));

        super.sendToDeadLetterQueue(context, messageReference);
    }

    private Optional<Level> config(final String value) {
        return Optional.ofNullable(Level.toLevel(params.get(value)));
    }

    private void log(Level level, String message) {
        LOG.log(getClass().getName(), level, message, null);
    }
}
