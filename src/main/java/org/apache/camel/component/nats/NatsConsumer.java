package org.apache.camel.component.nats;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.nats.Connection;
import org.nats.MsgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NatsConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(NatsConsumer.class);

    private final NatsEndpoint endpoint;
    private final Processor processor;
    private Connection connection;
    protected ExecutorService executor;
    private int sid;

    public NatsConsumer(NatsEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    public NatsEndpoint getEndpoint() {
        return (NatsEndpoint) super.getEndpoint();
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        LOG.info("Starting Nats Consumer");
        executor = endpoint.createExecutor();

        LOG.info("Getting Nats Connection");
        connection = getConnection();

        executor.submit(new NatsConsumingTask(connection, getEndpoint().getNatsConfiguration()));

    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        LOG.info("Stopping Nats Consumer");
        if (executor != null) {
            if (getEndpoint() != null && getEndpoint().getCamelContext() != null) {
                getEndpoint().getCamelContext().getExecutorServiceManager().shutdownNow(executor);
            } else {
                executor.shutdownNow();
            }
        }
        executor = null;
        
        LOG.info("Closing Nats Connection");
        if (connection != null) {
            connection.close();   
        }
    }

    private Connection getConnection() throws IOException, InterruptedException {

        Properties prop = getEndpoint().getNatsConfiguration().createProperties();
        connection = Connection.connect(prop);

        return connection;
    }

    class NatsConsumingTask implements Runnable {

        private final Connection connection;
        
        private final NatsConfiguration configuration;

        public NatsConsumingTask(Connection connection, NatsConfiguration configuration) {
            this.connection = connection;
            this.configuration = configuration;
        }

        @Override
        public void run() {
            try {
                sid = connection.subscribe(getEndpoint().getNatsConfiguration().getTopic(), configuration.createSubProperties(), new MsgHandler() {
                    public void execute(String msg) {
                        Exchange exchange = getEndpoint().createExchange();
                        exchange.getIn().setBody(msg);
                        exchange.getIn().setHeader(NatsConstants.NATS_MESSAGE_TIMESTAMP, System.currentTimeMillis());
                        exchange.getIn().setHeader(NatsConstants.NATS_SUBSCRIBE_SID, sid);
                        try {
                            getProcessor().process(exchange);
                        } catch (Exception e) {
                            getExceptionHandler().handleException("Error during processing", exchange, e);
                        }
                    }
                });
            } catch (IOException e1) {
                getExceptionHandler().handleException("Error during processing", e1);
            }
        }
    }

}
