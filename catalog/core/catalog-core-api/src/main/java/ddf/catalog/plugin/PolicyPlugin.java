/**
 * Copyright (c) Codice Foundation
 * <p/>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package ddf.catalog.plugin;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ddf.catalog.data.Metacard;
import ddf.catalog.data.Result;
import ddf.catalog.operation.Query;

/**
 * A PolicyPlugin is used to build policy information regarding the Catalog action a user is attempting.
 *
 * The {@link PolicyResponse} object contains 1 or more policy objects of the type: Map<String, Set<String>>.
 * Where the key is some attribute that you wish to assert against a Subject and Set<String> would be the values
 * associated with that key.
 */
public interface PolicyPlugin {

    String OPERATION_SECURITY = "operation.security";

    /**
     * Processes a {@link Metacard}, prior to ddf.catalog.source.CatalogProvider#create(ddf.catalog.operation.CreateRequest), to return policy information
     *
     * @param input the {@link Metacard} to process
     * @param properties the request properties
     * @return policy information to pass to the {@link AccessPlugin}
     * @throws PluginExecutionException thrown when an error occurs while processing the {@link Metacard}
     * @throws StopProcessingException  thrown to halt processing when a critical issue occurs during processing. This is
     *                                  intended to prevent other plugins from processing as well.
     */
    PolicyResponse processPreCreate(Metacard input, Map<String, Serializable> properties)
            throws StopProcessingException;

    /**
     * Processes a {@link Metacard}, prior to ddf.catalog.source.CatalogProvider#update(ddf.catalog.operation.UpdateRequest), to return policy information
     *
     * @param input the {@link Metacard} to process
     * @param properties the request properties
     * @return policy information to pass to the {@link AccessPlugin}
     * @throws PluginExecutionException thrown when an error occurs while processing the {@link Metacard}
     * @throws StopProcessingException  thrown to halt processing when a critical issue occurs during processing. This is
     *                                  intended to prevent other plugins from processing as well.
     */
    PolicyResponse processPreUpdate(Metacard input, Map<String, Serializable> properties)
            throws StopProcessingException;

    /**
     * Processes a {@link ddf.catalog.operation.DeleteRequest}, prior to ddf.catalog.source.CatalogProvider#delete(ddf.catalog.operation.DeleteRequest), to return policy information
     *
     * @param attributeName the name of the attribute being used in the delete
     * @param attributeValues the values of the attribute to use for the delete
     * @param properties the request properties
     * @return policy information to pass to the {@link AccessPlugin}
     * @throws PluginExecutionException thrown when an error occurs while processing the {@link ddf.catalog.operation.DeleteRequest}
     * @throws StopProcessingException  thrown to halt processing when a critical issue occurs during processing. This is
     *                                  intended to prevent other plugins from processing as well.
     */
    PolicyResponse processPreDelete(String attributeName, List<Serializable> attributeValues, Map<String, Serializable> properties)
            throws StopProcessingException;

    /**
     * Processes a {@link Query}, prior to execution of the {@link ddf.catalog.operation.Query}, to return policy information
     *
     * @param query the {@link Query} to process
     * @param properties the request properties
     * @return policy information to pass to the {@link AccessPlugin}
     * @throws PluginExecutionException thrown when an error occurs while processing the {@link Query}
     * @throws StopProcessingException  thrown to halt processing when a critical issue occurs during processing. This is
     *                                  intended to prevent other plugins from processing as well.
     */
    PolicyResponse processPreQuery(Query query, Map<String, Serializable> properties)
            throws StopProcessingException;

    /**
     * Processes a {@link Result}, following the execution of the {@link ddf.catalog.operation.Query}, to return policy information
     *
     * @param input the {@link Result} to process
     * @param properties the request properties
     * @return policy information to pass to the {@link AccessPlugin}
     * @throws PluginExecutionException thrown when an error occurs while processing the {@link Result}
     * @throws StopProcessingException  thrown to halt processing when a critical issue occurs during processing. This is
     *                                  intended to prevent other plugins from processing as well.
     */
    PolicyResponse processPostQuery(Result input, Map<String, Serializable> properties)
            throws StopProcessingException;
}
