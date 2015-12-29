package org.apache.camel.component.nats;

import java.util.concurrent.ExecutorService;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NatsEndpoint extends DefaultEndpoint{

    private static final Logger LOG = LoggerFactory.getLogger(NatsEndpoint.class);

    @UriParam
    private NatsConfiguration configuration;
    
    public NatsEndpoint(String uri, NatsComponent component, NatsConfiguration config) {
        super(uri, component);
        this.configuration = config;
    }    
    
    @Override
    public Producer createProducer() throws Exception {
        return new NatsProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new NatsConsumer(this, processor);
    }
    
    public ExecutorService createExecutor() {
        return getCamelContext().getExecutorServiceManager().newFixedThreadPool(this, "NatsTopic[" + configuration.getTopic() + "]", 10);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
    
    /**
     * The nats Configuration
     */
    public NatsConfiguration getNatsConfiguration() {
        return configuration;
    }
}
