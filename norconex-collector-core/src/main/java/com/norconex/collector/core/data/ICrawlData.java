/* Copyright 2014 Norconex Inc.
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
package com.norconex.collector.core.data;

import java.io.Serializable;

/**
 * A pointer that uniquely identifies a resource being processed (e.g. a 
 * URL, a file path, etc).  Implementors are strongly encourage to subclass
 * {@link BaseCrawlData} since some features of Norocnex Collector Core 
 * references it.
 * @author Pascal Essiembre
 * @see BaseCrawlData
 */
public interface ICrawlData extends Cloneable, Serializable {

    /**
     * Gets the unique identifier of this reference (e.g. URL, path, etc).
     * @return reference unique identifier
     */
    String getReference();
    
    /**
     * Gets this reference state.
     * @return state
     */
    CrawlState getState();
    
    /**
     * Clones this reference.
     * @return a copy of this instance
     */
    ICrawlData clone();
    
    
    boolean isRootParentReference();
    
    String getParentRootReference();
    
    
    String getMetaChecksum();

    String getContentChecksum();

}