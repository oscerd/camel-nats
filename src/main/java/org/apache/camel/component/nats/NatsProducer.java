package org.apache.camel.component.nats;

import java.io.IOException;
import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.nats.Connection;

public class NatsProducer extends DefaultProducer {
    
    private Connection connection;
    
    public NatsProducer(NatsEndpoint endpoint) {
        super(endpoint);
    }
    
    @Override
    public NatsEndpoint getEndpoint() {
        return (NatsEndpoint) super.getEndpoint();
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        NatsConfiguration config = getEndpoint().getNatsConfiguration();
        connection.publish(config.getTopic(), exchange.getIn().getBody(String.class));
    }
    

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        connection = getConnection();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        connection.close();
    }

    
    private Connection getConnection() throws IOException, InterruptedException {

        Properties prop = getEndpoint().getNatsConfiguration().createProperties();
        connection = Connection.connect(prop);

        return connection;
    }
}
