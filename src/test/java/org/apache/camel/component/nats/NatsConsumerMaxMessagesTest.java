package org.apache.camel.component.nats;

import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NatsConsumerMaxMessagesTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint mockResultEndpoint;

    @Test
    public void testMaxConsumer() throws InterruptedException, IOException {
        mockResultEndpoint.expectedBodiesReceived("test","test1","test2","test3","test4");
        mockResultEndpoint.setExpectedMessageCount(5);
        template.sendBody("direct:send", "test");
        template.sendBody("direct:send", "test1");
        template.sendBody("direct:send", "test2");
        template.sendBody("direct:send", "test3");
        template.sendBody("direct:send", "test4");
        template.sendBody("direct:send", "test5");
        template.sendBody("direct:send", "test6");
        template.sendBody("direct:send", "test7");
        template.sendBody("direct:send", "test8");
        template.sendBody("direct:send", "test9");
        template.sendBody("direct:send", "test10");

        mockResultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:send").to("nats://localhost:4222?topic=test");
                from("nats://localhost:4222?topic=test&maxMessages=5").to(mockResultEndpoint);
            }
        };
    }
}
