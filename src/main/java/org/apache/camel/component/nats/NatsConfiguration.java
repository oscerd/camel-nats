package org.apache.camel.component.nats;

import java.util.Properties;

import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;

public class NatsConfiguration {

    private String servers;
    @UriParam @Metadata(required = "true")
    private String topic;
    @UriParam(defaultValue = "true")
    private boolean reconnect = true;
    @UriParam(defaultValue = "false")
    private boolean pedantic = false;
    @UriParam(defaultValue = "false")
    private boolean verbose = false;
    @UriParam(defaultValue = "false")
    private boolean ssl = false;
    @UriParam(defaultValue = "2000")
    private int reconnectTimeWait = 2000;
    @UriParam(defaultValue = "3")
    private int maxReconnectAttempts = 3;
    @UriParam(defaultValue = "4000")
    private int pingInterval = 4000;
    @UriParam(defaultValue = "false")
    private boolean noRandomizeServers = false;
    @UriParam(label = "consumer")
    private String queueName;
    @UriParam(label = "consumer")
    private String maxMessages;
    
    public String getServers() {
        return servers;
    }
    public void setServers(String servers) {
        this.servers = servers;
    }
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public boolean getReconnect() {
        return reconnect;
    }
    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }
    public boolean getPedantic() {
        return pedantic;
    }
    public void setPedantic(boolean pedantic) {
        this.pedantic = pedantic;
    }
    public boolean getVerbose() {
        return verbose;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    public boolean getSsl() {
        return ssl;
    }
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
    public int getReconnectTimeWait() {
        return reconnectTimeWait;
    }
    public void setReconnectTimeWait(int reconnectTimeWait) {
        this.reconnectTimeWait = reconnectTimeWait;
    }
    public int getMaxReconnectAttempts() {
        return maxReconnectAttempts;
    }
    public void setMaxReconnectAttempts(int maxReconnectAttempts) {
        this.maxReconnectAttempts = maxReconnectAttempts;
    }
    public int getPingInterval() {
        return pingInterval;
    }
    public void setPingInterval(int pingInterval) {
        this.pingInterval = pingInterval;
    }
    public boolean getNoRandomizeServers() {
        return noRandomizeServers;
    }
    public void setNoRandomizeServers(boolean noRandomizeServers) {
        this.noRandomizeServers = noRandomizeServers;
    }
    public String getQueueName() {
        return queueName;
    }
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
    public String getMaxMessages() {
        return maxMessages;
    }
    public void setMaxMessages(String maxMessages) {
        this.maxMessages = maxMessages;
    }
    private static <T> void addPropertyIfNotNull(Properties props, String key, T value) {
        if (value != null) {
            props.put(key, value);
        }
    }
    
    public Properties createProperties() {
        Properties props = new Properties();
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_URI, splitServers());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_VERBOSE, getVerbose());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_PEDANTIC, getPedantic());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_SSL, getSsl());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_RECONNECT, getReconnect());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_MAX_RECONNECT_ATTEMPTS, getMaxReconnectAttempts());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_RECONNECT_TIME_WAIT, getReconnectTimeWait());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_PING_INTERVAL, getPingInterval());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_DONT_RANDOMIZE_SERVERS, getNoRandomizeServers());
        return props;
    }
    
    public Properties createSubProperties() {
        Properties props = new Properties();
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_QUEUE, getQueueName());
        addPropertyIfNotNull(props, NatsPropertiesConstants.NATS_PROPERTY_MAX_MESSAGES, getMaxMessages());
        return props;
    }
    
    private String splitServers() {
        StringBuilder servers = new StringBuilder();
        String prefix = "nats://";
        
        String[] pieces = getServers().split(",");
        for (int i=0;i<pieces.length; i++) {
            if (i < pieces.length - 1) {
                servers.append(prefix + pieces[i] + ",");
            } else {
                servers.append(prefix + pieces[i]);
            }
        }
        return servers.toString();
    }
}
