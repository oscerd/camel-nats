package org.apache.camel.component.nats;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

public class NatsComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        NatsConfiguration config = new NatsConfiguration();
        setProperties(config, parameters);
        config.setServers(remaining);
        NatsEndpoint endpoint = new NatsEndpoint(uri, this, config);
        return endpoint;
    }

}
