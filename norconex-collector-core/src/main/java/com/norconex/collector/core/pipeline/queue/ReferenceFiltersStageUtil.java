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
package com.norconex.collector.core.pipeline.queue;

import static com.norconex.collector.core.crawler.CrawlerEvent.REJECTED_FILTER;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norconex.collector.core.filter.IReferenceFilter;
import com.norconex.collector.core.pipeline.BasePipelineContext;
import com.norconex.importer.handler.filter.IOnMatchFilter;
import com.norconex.importer.handler.filter.OnMatch;

/**
 * Reference-filtering stage utility methods.
 * @author Pascal Essiembre
 */
public final class ReferenceFiltersStageUtil {

    private static final Logger LOG =
            LoggerFactory.getLogger(ReferenceFiltersStageUtil.class);

    private ReferenceFiltersStageUtil() {
        super();
    }

    // return true if reference is rejected
    public static boolean resolveReferenceFilters(
            List<IReferenceFilter> filters,
            BasePipelineContext ctx, String type) {
        if (filters == null) {
            return false;
        }
        String msg = StringUtils.trimToEmpty(type);
        if (StringUtils.isNotBlank(msg)) {
            msg = " (" + msg + ")";
        }

        boolean hasIncludes = false;
        boolean atLeastOneIncludeMatch = false;
        for (IReferenceFilter filter : filters) {
            boolean accepted = filter.acceptReference(
                    ctx.getCrawlData().getReference());

            // Deal with includes
            if (isIncludeFilter(filter)) {
                hasIncludes = true;
                if (accepted) {
                    atLeastOneIncludeMatch = true;
                }
                continue;
            }

            // Deal with exclude and non-OnMatch filters
            if (accepted) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ACCEPTED document reference" + msg
                            + ". Reference=" + ctx.getCrawlData().getReference()
                            + " Filter=" + filter);
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("REJECTED document reference " + msg
                            + ". Reference=" + ctx.getCrawlData().getReference()
                            + " Filter=" + filter);
                }
                fireDocumentRejected(filter, ctx);
                return true;
            }
        }
        if (hasIncludes && !atLeastOneIncludeMatch) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("REJECTED document reference" + msg
                      + ". No include filters matched. Reference="
                      + ctx.getCrawlData().getReference()
                      + " Filter=[one or more filter 'onMatch' "
                      + "attribute is set to 'include', but none of them were "
                      + "matched]");
            }
            fireDocumentRejected(
                    "No \"include\" reference filters matched.", ctx);
            return true;
        }
        return false;
    }

    private static void fireDocumentRejected(
            Object subject, BasePipelineContext ctx) {
        ctx.fireCrawlerEvent(REJECTED_FILTER, ctx.getCrawlData(), subject);

    }

    private static boolean isIncludeFilter(IReferenceFilter filter) {
        return filter instanceof IOnMatchFilter
                && OnMatch.INCLUDE == ((IOnMatchFilter) filter).getOnMatch();
    }
}
