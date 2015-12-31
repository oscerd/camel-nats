package org.apache.camel.component.nats;

import java.io.IOException;
import java.util.Properties;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.nats.Connection;

public class NatsConsumerLoadTest extends CamelTestSupport {
    
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint mockResultEndpoint;

    @Test
    public void testLoadConsumer() throws InterruptedException, IOException {
        mockResultEndpoint.setExpectedMessageCount(10000);
        
        Connection connection = Connection.connect(new Properties());
        
        for (int i=0;i<10000;i++) {
            connection.publish("test", ("test" + i).getBytes());
        }

        mockResultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("nats://localhost:4222?topic=test").to(mockResultEndpoint);
            }
        };
    }

}
