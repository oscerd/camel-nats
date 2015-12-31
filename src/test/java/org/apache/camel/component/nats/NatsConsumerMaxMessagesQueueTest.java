package org.apache.camel.component.nats;

import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NatsConsumerMaxMessagesQueueTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint mockResultEndpoint;

    @Test
    public void testMaxConsumer() throws InterruptedException, IOException {
        mockResultEndpoint.expectedBodiesReceivedInAnyOrder("test", "test1");
        mockResultEndpoint.setExpectedMessageCount(2);
        
        template.sendBody("direct:send", "test");
        template.sendBody("direct:send", "test1");

        mockResultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:send").to("nats://localhost:4222?topic=test");
                from("nats://localhost:4222?topic=test&maxMessages=5&queueName=test").routeId("cons1").to(mockResultEndpoint);
                from("nats://localhost:4222?topic=test&maxMessages=6&queueName=test").routeId("cons2").to(mockResultEndpoint);
            }
        };
    }
}
