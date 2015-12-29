package org.apache.camel.component.nats;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NatsProducerTest extends CamelTestSupport {
    
    @Test
    public void sendTest() throws Exception {
        template.sendBody("direct:send", "pippo");
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:send").to("nats://localhost:4222?topic=test");
            }
        };
    }
}
