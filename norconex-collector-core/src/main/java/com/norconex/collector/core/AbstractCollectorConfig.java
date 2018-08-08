/* Copyright 2014-2018 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.collector.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norconex.collector.core.crawler.CrawlerConfigLoader;
import com.norconex.collector.core.crawler.ICrawlerConfig;
import com.norconex.commons.lang.collection.CollectionUtil;
import com.norconex.commons.lang.xml.XML;
//import com.norconex.jef5.job.IJobErrorListener;
//import com.norconex.jef5.job.IJobLifeCycleListener;
//import com.norconex.jef5.suite.ISuiteLifeCycleListener;

/**
 * Base Collector configuration.
 * @author Pascal Essiembre
 */
public abstract class AbstractCollectorConfig implements ICollectorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(
            AbstractCollectorConfig.class);

    /** Default relative directory where logs from Log4j are stored. */
    public static final Path DEFAULT_LOGS_DIR = Paths.get("./logs");
    /** Default relative directory where progress files are stored. */
    public static final Path DEFAULT_PROGRESS_DIR = Paths.get("./progress");

    //TODO still needed?
    private final Class<? extends ICrawlerConfig> crawlerConfigClass;
    //private final String xmlConfigRootTag;

    private String id;
    private final List<ICrawlerConfig> crawlerConfigs = new ArrayList<>();
    private Path progressDir = DEFAULT_PROGRESS_DIR;
    private Path logsDir = DEFAULT_LOGS_DIR;
    private final List<ICollectorLifeCycleListener> collectorListeners =
            new ArrayList<>();
//    private IJobLifeCycleListener[] jobLifeCycleListeners;
//    private IJobErrorListener[] jobErrorListeners;
//    private ISuiteLifeCycleListener[] suiteLifeCycleListeners;

    public AbstractCollectorConfig() {
        this((Class<? extends ICrawlerConfig>) null);
    }
    public AbstractCollectorConfig(
            Class<? extends ICrawlerConfig> crawlerConfigClass) {
        this.crawlerConfigClass = crawlerConfigClass;
//        this(crawlerConfigClass, "collector");
    }
//    public AbstractCollectorConfig(String xmlConfigRootTag) {
//        this(null, xmlConfigRootTag);
//    }
//    public AbstractCollectorConfig(
//            Class<? extends ICrawlerConfig> crawlerConfigClass,
//            String xmlConfigRootTag) {
//        super();
//        this.crawlerConfigClass = crawlerConfigClass;
//        this.xmlConfigRootTag = xmlConfigRootTag;
//    }


	/**
	 * Gets this collector unique identifier.
	 * @return unique identifier
	 */
    @Override
    public String getId() {
        return id;
    }
    /**
     * Sets this collector unique identifier. It is important
     * the id of the collector is unique amongst your collectors.  This
     * facilitates integration with different systems and facilitates
     * tracking.
     * @param id unique identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<ICrawlerConfig> getCrawlerConfigs() {
        return Collections.unmodifiableList(crawlerConfigs);
    }
    /**
     * Sets crawler configurations.
     * @param crawlerConfigs crawler configurations
     */
    public void setCrawlerConfigs(ICrawlerConfig... crawlerConfigs) {
        setCrawlerConfigs(Arrays.asList(crawlerConfigs));
    }
    /**
     * Sets crawler configurations.
     * @param crawlerConfigs crawler configurations
     * @since 2.0.0
     */
    public void setCrawlerConfigs(List<ICrawlerConfig> crawlerConfigs) {
        CollectionUtil.setAll(this.crawlerConfigs, crawlerConfigs);
    }

    /**
     * Gets the directory location where progress files (from JEF API)
     * are stored.
     * @return progress directory path
     */
    @Override
    public Path getProgressDir() {
        return progressDir;
    }
    /**
     * Sets the directory location where progress files (from JEF API)
     * are stored.
     * @param progressDir progress directory path
     */
    public void setProgressDir(Path progressDir) {
        this.progressDir = progressDir;
    }

    /**
     * Gets the directory location of generated log files.
     * @return logs directory path
     */
    @Override
    public Path getLogsDir() {
        return logsDir;
    }
    /**
     * Sets the directory location of generated log files.
     * @param logsDir logs directory path
     */
    public void setLogsDir(Path logsDir) {
        this.logsDir = logsDir;
    }

    @Override
    public List<ICollectorLifeCycleListener> getCollectorListeners() {
        return collectorListeners;
    }
    /**
     * Sets collector life cycle listeners.
     * @param collectorListeners collector life cycle listeners.
     * @since 1.8.0
     */
    public void setCollectorListeners(
            ICollectorLifeCycleListener... collectorListeners) {
        setCollectorListeners(Arrays.asList(collectorListeners));
    }
    /**
     * Sets collector life cycle listeners.
     * @param collectorListeners collector life cycle listeners.
     * @since 2.0.0
     */
    public void setCollectorListeners(
            List<ICollectorLifeCycleListener> collectorListeners) {
        CollectionUtil.setAll(this.collectorListeners, collectorListeners);
    }

//    @Override
//    public IJobLifeCycleListener[] getJobLifeCycleListeners() {
//        return jobLifeCycleListeners;
//    }
//    /**
//     * Sets JEF job life cycle listeners. A job typically represents a
//     * crawler instance. Interacting directly
//     * with the <a href="https://www.norconex.com/jef/api/">JEF API</a>
//     * is normally reserved for more advanced use.
//     * @param jobLifeCycleListeners JEF job life cycle listeners.
//     * @since 1.7.0
//     */
//    public void setJobLifeCycleListeners(
//            IJobLifeCycleListener... jobLifeCycleListeners) {
//        this.jobLifeCycleListeners = jobLifeCycleListeners;
//    }
//
//    @Override
//    public IJobErrorListener[] getJobErrorListeners() {
//        return jobErrorListeners;
//    }
//    /**
//     * Sets JEF error listeners. Interacting directly
//     * with the <a href="https://www.norconex.com/jef/api/">JEF API</a>
//     * is normally reserved for more advanced use.
//     * @param errorListeners JEF job error listeners
//     * @since 1.7.0
//     */
//    public void setJobErrorListeners(IJobErrorListener... errorListeners) {
//        this.jobErrorListeners = errorListeners;
//    }
//
//    @Override
//    public ISuiteLifeCycleListener[] getSuiteLifeCycleListeners() {
//        return suiteLifeCycleListeners;
//    }
//    /**
//     * Sets JEF job suite life cycle listeners.
//     * A job suite typically represents a collector instance.
//     * Interacting directly
//     * with the <a href="https://www.norconex.com/jef/api/">JEF API</a>
//     * is normally reserved for more advanced use.
//     * @param suiteLifeCycleListeners JEF suite life cycle listeners
//     * @since 1.7.0
//     */
//    public void setSuiteLifeCycleListeners(
//            ISuiteLifeCycleListener... suiteLifeCycleListeners) {
//        this.suiteLifeCycleListeners = suiteLifeCycleListeners;
//    }

    @Override
    public void saveToXML(XML xml) {
        xml.setAttribute("id", getId());

        xml.addElement("logsDir", getLogsDir());
        xml.addElement("progressDir", getProgressDir());
        xml.addElementList(
                "collectorListeners", "listener", getCollectorListeners());
//        writeArray(out, "jobLifeCycleListeners",
//                "listener", getJobLifeCycleListeners());
//        writeArray(out, "jobErrorListeners",
//                "listener", getJobErrorListeners());
//        writeArray(out, "suiteLifeCycleListeners",
//                "listener", getSuiteLifeCycleListeners());

        xml.addElementList("crawlers", "crawler", getCrawlerConfigs());

        saveCollectorConfigToXML(xml);

//        System.out.println("XML:\n" + xml.toString(4));
    }
    protected abstract void saveCollectorConfigToXML(XML xml);

    @Override
    public final void loadFromXML(XML xml) {

        String collectorId = xml.getString("@id", null);
        if (StringUtils.isBlank(collectorId)) {
            throw new CollectorException(
                    "Collector id attribute is mandatory.");
        }
        setId(collectorId);
        setLogsDir(xml.getPath("logsDir", getLogsDir()));
        setProgressDir(xml.getPath("progressDir", getProgressDir()));
        setCollectorListeners(xml.getObjectList(
                "collectorListeners/listener", getCollectorListeners()));

//        // JEF Job listeners
//        IJobLifeCycleListener[] jlcListeners = loadJobLifeCycleListeners(
//                xml, "jobLifeCycleListeners.listener");
//        setJobLifeCycleListeners(defaultIfEmpty(jlcListeners,
//                getJobLifeCycleListeners()));
//
//        // JEF error listeners
//        IJobErrorListener[] jeListeners = loadJobErrorListeners(
//                xml, "jobErrorListeners.listener");
//        setJobErrorListeners(defaultIfEmpty(jeListeners,
//                getJobErrorListeners()));
//
//        // JEF suite listeners
//        ISuiteLifeCycleListener[] suiteListeners = loadSuiteLifeCycleListeners(
//                xml, "suiteLifeCycleListeners.listener");
//        setSuiteLifeCycleListeners(defaultIfEmpty(suiteListeners,
//                getSuiteLifeCycleListeners()));

        if (crawlerConfigClass != null) {
            List<ICrawlerConfig> cfgs = new CrawlerConfigLoader(
                    crawlerConfigClass).loadCrawlerConfigs(xml);
            if (CollectionUtils.isNotEmpty(cfgs)) {
                setCrawlerConfigs(cfgs);
            }
        }

        loadCollectorConfigFromXML(xml);

        if (LOG.isInfoEnabled()) {
            LOG.info("Configuration loaded: id=" + collectorId + "; logsDir="
                    + getLogsDir() + "; progressDir=" + getProgressDir());
        }
    }

//    private ICollectorLifeCycleListener[] loadCollectorListeners(
//            XML xml, String xmlPath) {
//        List<ICollectorLifeCycleListener> listeners = new ArrayList<>();
//        List<HierarchicalConfiguration> listenerNodes = xml
//                .configurationsAt(xmlPath);
//        for (HierarchicalConfiguration listenerNode : listenerNodes) {
//            ICollectorLifeCycleListener listener =
//                    XMLConfigurationUtil.newInstance(listenerNode);
//            listeners.add(listener);
//            LOG.info("Collector life cycle listener loaded: " + listener);
//        }
//        return listeners.toArray(new ICollectorLifeCycleListener[] {});
//    }
//    private IJobLifeCycleListener[] loadJobLifeCycleListeners(
//            XMLConfiguration xml, String xmlPath) {
//        List<IJobLifeCycleListener> listeners = new ArrayList<>();
//        List<HierarchicalConfiguration> listenerNodes = xml
//                .configurationsAt(xmlPath);
//        for (HierarchicalConfiguration listenerNode : listenerNodes) {
//            IJobLifeCycleListener listener =
//                    XMLConfigurationUtil.newInstance(listenerNode);
//            listeners.add(listener);
//            LOG.info("Job life cycle listener loaded: " + listener);
//        }
//        return listeners.toArray(new IJobLifeCycleListener[] {});
//    }
//    private IJobErrorListener[] loadJobErrorListeners(
//            XMLConfiguration xml, String xmlPath) {
//        List<IJobErrorListener> listeners = new ArrayList<>();
//        List<HierarchicalConfiguration> listenerNodes = xml
//                .configurationsAt(xmlPath);
//        for (HierarchicalConfiguration listenerNode : listenerNodes) {
//            IJobErrorListener listener =
//                    XMLConfigurationUtil.newInstance(listenerNode);
//            listeners.add(listener);
//            LOG.info("Job error listener loaded: " + listener);
//        }
//        return listeners.toArray(new IJobErrorListener[] {});
//    }
//    private ISuiteLifeCycleListener[] loadSuiteLifeCycleListeners(
//            XMLConfiguration xml, String xmlPath) {
//        List<ISuiteLifeCycleListener> listeners = new ArrayList<>();
//        List<HierarchicalConfiguration> listenerNodes = xml
//                .configurationsAt(xmlPath);
//        for (HierarchicalConfiguration listenerNode : listenerNodes) {
//            ISuiteLifeCycleListener listener =
//                    XMLConfigurationUtil.newInstance(listenerNode);
//            listeners.add(listener);
//            LOG.info("Suite life cycle listener loaded: " + listener);
//        }
//        return listeners.toArray(new ISuiteLifeCycleListener[] {});
//    }

    protected abstract void loadCollectorConfigFromXML(XML xml);

//    //TODO transfer to utility method in (Nx Commons Lang?) since it is
//    //duplicated code from AbstractCrawlerConfig.
//    protected void writeObject(
//            Writer out, String tagName, Object object) throws IOException {
//        writeObject(out, tagName, object, false);
//    }
//    //TODO transfer to utility method in (Nx Commons Lang?) since it is
//    //duplicated code from AbstractCrawlerConfig.
//    protected void writeObject(
//            Writer out, String tagName, Object object, boolean ignore)
//                    throws IOException {
//        out.flush();
//        if (object == null) {
//            if (ignore) {
//                out.write("<" + tagName + " ignore=\"" + ignore + "\" />");
//            }
//            return;
//        }
//        StringWriter w = new StringWriter();
//        if (object instanceof IXMLConfigurable) {
//            ((IXMLConfigurable) object).saveToXML(w);
//        } else {
//            w.write("<" + tagName + " class=\""
//                    + object.getClass().getCanonicalName() + "\" />");
//        }
//        String xml = w.toString();
//        if (ignore) {
//            xml = xml.replace("<" + tagName + " class=\"" ,
//                    "<" + tagName + " ignore=\"true\" class=\"" );
//        }
//        out.write(xml);
//        out.flush();
//    }
//    //TODO transfer to utility method in (Nx Commons Lang?) since it is
//    //duplicated code from AbstractCrawlerConfig.
//    protected void writeArray(Writer out, String listTagName,
//            String objectTagName, Object[] array) throws IOException {
//        if (ArrayUtils.isEmpty(array)) {
//            return;
//        }
//        out.write("<" + listTagName + ">");
//        for (Object obj : array) {
//            writeObject(out, objectTagName, obj);
//        }
//        out.write("</" + listTagName + ">");
//        out.flush();
//    }
//    //TODO transfer to utility method in (Nx Commons Lang?) since it is
//    //duplicated code from AbstractCrawlerConfig.
//    protected <T> T[] defaultIfEmpty(T[] array, T[] defaultArray) {
//        if (ArrayUtils.isEmpty(array)) {
//            return defaultArray;
//        }
//        return array;
//    }

    @Override
    public boolean equals(final Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(
                this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
