/* Copyright 2010-2013 Norconex Inc.
 * 
 * This file is part of Norconex Collector Core.
 * 
 * Norconex Collector Core is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Norconex Collector Core is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Norconex Collector Core. If not, 
 * see <http://www.gnu.org/licenses/>.
 */
package com.norconex.collector.core;

import com.norconex.collector.core.crawler.ICrawler;
import com.norconex.collector.core.crawler.ICrawlerConfig;
 
/**
 * Main application class. In order to use it properly, you must first configure
 * it, either by providing a populated instance of
 * {@link AbstractCollectorConfig},
 * or by XML configuration, loaded using {@link CollectorConfigLoader}.
 * Instances of this class can hold several crawler, running at once.
 * This is convenient when there are configuration setting to be shared amongst
 * crawlers.  When you have many crawler jobs defined that have nothing
 * in common, it may be best to configure and run them separately, to facilitate
 * troubleshooting.  There is no fair rule for this, experimenting with your
 * target sites will help you.
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public abstract class AbstractCollector implements ICollector {

//	private static final Logger LOG = 
//	        LogManager.getLogger(AbstractCollector.class);
    private AbstractCollectorConfig collectorConfig;

    private ICrawler[] crawlers;

    
	/**
	 * Creates and configure a Collector with the provided
	 * configuration.
	 * @param collectorConfig Collector configuration
	 */
    public AbstractCollector(AbstractCollectorConfig collectorConfig) {
        //TODO clone config so modifications no longer apply.
        if (collectorConfig == null) {
            throw new IllegalArgumentException(
                    "Collector Configugation cannot be null.");
        }
        
        this.collectorConfig = collectorConfig;

        ICrawlerConfig[] crawlerConfigs = 
                this.collectorConfig.getCrawlerConfigs();
        if (crawlerConfigs != null) {
            ICrawler[] newCrawlers = new ICrawler[crawlerConfigs.length];
            for (int i = 0; i < crawlerConfigs.length; i++) {
                ICrawlerConfig crawlerConfig = crawlerConfigs[i];
                newCrawlers[i] = createCrawler(crawlerConfig);
            }
            this.crawlers = newCrawlers;
        } else {
            this.crawlers = new ICrawler[]{};
        }
    }

    protected abstract ICrawler createCrawler(ICrawlerConfig config);
    
    /**
     * Gets the collector configuration
     * @return the collectorConfig
     */
    @Override
    public AbstractCollectorConfig getCollectorConfig() {
        return collectorConfig;
    }
    
    @Override
    public String getId() {
        return collectorConfig.getId();
    }
    
    public void setCrawlers(ICrawler[] crawlers) {
        this.crawlers = crawlers;
    }
    public ICrawler[] getCrawlers() {
        return crawlers;
    }

//    /**
//     * Launched all crawlers defined in configuration.
//     * @param resumeNonCompleted whether to resume where previous crawler
//     *        aborted (if applicable) 
//     */
//    @Override
//    public void start(boolean resumeNonCompleted) {
////        JobSuite suite = createJobSuite();
////        JobRunner jobRunner = new JobRunner();
////        jobRunner.runSuite(suite, resumeNonCompleted);
//    }
//
//    /**
//     * Stops a running instance of this HTTP Collector.
//     */
//    @Override
//    public void stop() {
////        JobSuite suite = createJobSuite();
////        ((FileStopRequestHandler) 
////                suite.getStopRequestHandler()).fireStopRequest();
//    }
//    
//    
//    @Override
//    public JobSuite createJobSuite() {
//        if (abstractCollectorConfig == null) {
//            try {
//                abstractCollectorConfig = CollectorConfigLoader.loadCollectorConfig(
//                        getConfigurationFile(), getVariablesFile());
//            } catch (Exception e) {
//                throw new CollectorException(e);
//            }
//        }
//        if (abstractCollectorConfig == null) {
//        	throw new CollectorException(
//        			"Configuration file does not exists: "
//        			+ getConfigurationFile());
//        }
//        HttpCrawlerConfig[] configs = abstractCollectorConfig.getCrawlerConfigs();
//        crawlers = new HttpCrawler[configs.length];
//        for (int i = 0; i < configs.length; i++) {
//            HttpCrawlerConfig crawlerConfig = configs[i];
//            crawlers[i] = new HttpCrawler(crawlerConfig);
//        }
//
//        IJob rootJob = null;
//        if (crawlers.length > 1) {
//            rootJob = new AsyncJobGroup(
//                    abstractCollectorConfig.getId(), crawlers
//            );
//        } else if (crawlers.length == 1) {
//            rootJob = crawlers[0];
//        }
//        
//        JobSuite suite = new JobSuite(
//                rootJob, 
//                new JobProgressPropertiesFileSerializer(
//                        abstractCollectorConfig.getProgressDir()),
//                new FileLogManager(abstractCollectorConfig.getLogsDir()),
//                new FileStopRequestHandler(abstractCollectorConfig.getId(), 
//                        abstractCollectorConfig.getProgressDir()));
//        LOG.info("Suite of " + crawlers.length + " HTTP crawler jobs created.");
//        return suite;
//    }
}
