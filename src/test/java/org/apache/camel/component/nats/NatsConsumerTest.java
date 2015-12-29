package org.apache.camel.component.nats;

import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NatsConsumerTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint mockResultEndpoint;

    @Test
    public void testConsumer() throws InterruptedException, IOException {
        mockResultEndpoint.expectedMessageCount(1);
        mockResultEndpoint.expectedBodiesReceived("test");
        template.requestBody("direct:send", "test");

        mockResultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:send").to("nats://localhost:4222?topic=test");
                from("nats://localhost:4222?topic=test").to(mockResultEndpoint);
            }
        };
    }
}
