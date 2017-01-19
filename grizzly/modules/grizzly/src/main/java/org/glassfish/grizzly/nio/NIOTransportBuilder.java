/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.grizzly.nio;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.TimeUnit;

import org.glassfish.grizzly.IOStrategy;
import org.glassfish.grizzly.Transport;
import org.glassfish.grizzly.asyncqueue.AsyncQueueWriter;
import org.glassfish.grizzly.attributes.AttributeBuilder;
import org.glassfish.grizzly.filterchain.DefaultFilterChain;
import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.memory.MemoryManager;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

/**
 * This builder is responsible for creating {@link NIOTransport} implementations
 * as well as providing basic configuration for <code>IOStrategies</code> and
 * thread pools.
 *
 * @see NIOTransport
 * @see IOStrategy
 * @see ThreadPoolConfig
 *
 * @since 2.0
 */
public abstract class NIOTransportBuilder<T extends NIOTransportBuilder> {

    protected ThreadPoolConfig workerConfig;
    protected ThreadPoolConfig kernelConfig;
    protected SelectorProvider selectorProvider;
    protected SelectorHandler selectorHandler =
            SelectorHandler.DEFAULT_SELECTOR_HANDLER;
    protected MemoryManager memoryManager =
            MemoryManager.DEFAULT_MEMORY_MANAGER;
    protected AttributeBuilder attributeBuilder =
            AttributeBuilder.DEFAULT_ATTRIBUTE_BUILDER;
    protected IOStrategy ioStrategy = WorkerThreadIOStrategy.getInstance();
    protected int selectorRunnerCount = NIOTransport.DEFAULT_SELECTOR_RUNNER_COUNT;
    protected NIOChannelDistributor nioChannelDistributor;
    protected String name;
    protected FilterChain filterChain;
    protected int readBufferSize = Transport.DEFAULT_READ_BUFFER_SIZE;
    protected int writeBufferSize = Transport.DEFAULT_WRITE_BUFFER_SIZE;
    protected int clientSocketSoTimeout = NIOTransport.DEFAULT_CLIENT_SOCKET_SO_TIMEOUT;
    protected int connectionTimeout = NIOTransport.DEFAULT_CONNECTION_TIMEOUT;
    protected boolean reuseAddress = NIOTransport.DEFAULT_REUSE_ADDRESS;
    protected int maxPendingBytesPerConnection = AsyncQueueWriter.AUTO_SIZE;
    protected boolean optimizedForMultiplexing = NIOTransport.DEFAULT_OPTIMIZED_FOR_MULTIPLEXING;

    protected long blockingReadTimeout = TimeUnit.MILLISECONDS.convert(Transport.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
    protected long blockingWriteTimeout = TimeUnit.MILLISECONDS.convert(Transport.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);

    // ------------------------------------------------------------ Constructors



    protected NIOTransportBuilder() {
    }

    // ---------------------------------------------------------- Public Methods

    /**
     * Sets the number of {@link Selector}s to be created to serve Transport
     * connections. <tt>-1</tt> is the default value, which lets the Transport
     * to pick the value, usually it's equal to the number of CPU cores
     * {@link Runtime#availableProcessors()}.
     * 
     * @param selectorRunnersCount 
     */
    public T selectorRunnersCount(final int selectorRunnersCount) {
        this.selectorRunnerCount = selectorRunnersCount;
        return getThis();
    }
    
    /**
     * Sets the {@link ThreadPoolConfig} that will be used to construct the
     *  {@link java.util.concurrent.ExecutorService} for <code>IOStrategies</code>
     *  that require worker threads
     */
    public T workerThreadPoolConfig(final ThreadPoolConfig workerConfig) {
        this.workerConfig = workerConfig;
        return getThis();
    }
    
    /**
     * Sets the {@link ThreadPoolConfig} that will be used to construct the
     *  {@link java.util.concurrent.ExecutorService} which will run the {@link NIOTransport}'s
     *  {@link org.glassfish.grizzly.nio.SelectorRunner}s.
     */
    public T selectorThreadPoolConfig(final ThreadPoolConfig kernelConfig) {
        this.kernelConfig = kernelConfig;
        return getThis();
    }

    /**
     * <p>
     * Changes the {@link IOStrategy} that will be used.
     *
     * @param ioStrategy the {@link IOStrategy} to use.
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T ioStrategy(final IOStrategy ioStrategy) {
        this.ioStrategy = ioStrategy;
        return getThis();
    }

    /**
     * Set the {@link MemoryManager} to be used by the created {@link NIOTransport}.
     *
     * @param memoryManager the {@link MemoryManager}.
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T memoryManager(final MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
        return getThis();
    }

    /**
     * Set the {@link SelectorHandler} to be used by the created {@link NIOTransport}.
     *
     * @param selectorHandler the {@link SelectorHandler}.
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T selectorHandler(final SelectorHandler selectorHandler) {
        this.selectorHandler = selectorHandler;
        return getThis();
    }

    /**
     * Set the {@link AttributeBuilder} to be used by the created {@link NIOTransport}.
     *
     * @param attributeBuilder the {@link AttributeBuilder}.
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T attributeBuilder(AttributeBuilder attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
        return getThis();
    }

    /**
     * Set the {@link NIOChannelDistributor} to be used by the created {@link NIOTransport}.
     *
     * @param nioChannelDistributor the {@link NIOChannelDistributor}.
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T nioChannelDistributor(NIOChannelDistributor nioChannelDistributor) {
        this.nioChannelDistributor = nioChannelDistributor;
        return getThis();
    }

    /**
     * Set the {@link SelectorProvider} to be used by the created {@link NIOTransport}.
     *
     * @param selectorProvider the {@link SelectorProvider}.
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T selectorProvider(SelectorProvider selectorProvider) {
        this.selectorProvider = selectorProvider;
        return getThis();
    }

    /**
     * @see Transport#setName(String)
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T name(String name) {
        this.name = name;
        return getThis();
    }

    /**
     * @see Transport#setFilterChain(FilterChain)
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T filterChain(FilterChain filterChain) {
        this.filterChain = filterChain;
        return getThis();
    }

    /**
     * @see NIOTransport#setReadBufferSize(int)
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T readBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
        return getThis();
    }

    /**
     * @see NIOTransport#setWriteBufferSize(int)
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T writeBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
        return getThis();
    }

    /**
     * @see NIOTransport#setOptimizedForMultiplexing(boolean)
     *
     * @return this <code>NIOTransportBuilder</code>
     */
    public T optimizedForMultiplexing(final boolean optimizedForMultiplexing) {
        this.optimizedForMultiplexing = optimizedForMultiplexing;
        return getThis();
    }

    /**
     * @return this <code>NIOTransportBuilder</code>
     * @see NIOTransport#setClientSocketSoTimeout(int)
     */
    public T clientSocketSoTimeout(int clientSocketSoTimeout) {
        this.clientSocketSoTimeout = clientSocketSoTimeout;
        return getThis();
    }

    /**
     * @return this <code>NIOTransportBuilder</code>
     * @see NIOTransport#setConnectionTimeout(int)
     */
    public T connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return getThis();
    }

    /**
     * @see Transport#setBlockingReadTimeout(long, java.util.concurrent.TimeUnit)
     */
    public T blockingReadTimeout(final long timeout, final TimeUnit timeUnit) {
        if (timeout <= 0) {
            blockingReadTimeout = -1;
        } else {
            blockingReadTimeout = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
        }
        return getThis();
    }

    /**
     * @see Transport#setBlockingWriteTimeout(long, java.util.concurrent.TimeUnit)
     */
    public T blockingWriteTimeout(final long timeout, final TimeUnit timeUnit) {
        if (timeout <= 0) {
            blockingWriteTimeout = -1;
        } else {
            blockingWriteTimeout = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
        }
        return getThis();
    }

    /**
     * @return this <code>TCPNIOTransportBuilder</code>
     * @see org.glassfish.grizzly.nio.transport.TCPNIOTransport#setReuseAddress(boolean)
     */
    public T reuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
        return getThis();
    }

    /**
     * @return this <code>TCPNIOTransportBuilder</code>
     * @see org.glassfish.grizzly.asyncqueue.AsyncQueueWriter#setMaxPendingBytesPerConnection(int)
     * <p/>
     * Note: the value is per connection, not transport total.
     */
    public T maxAsyncWriteQueueSizeInBytes(final int maxAsyncWriteQueueSizeInBytes) {
        this.maxPendingBytesPerConnection = maxAsyncWriteQueueSizeInBytes;
        return getThis();
    }

    /**
     * @return an {@link NIOTransport} based on the builder's configuration.
     */
    public NIOTransport build() {
        NIOTransport transport = create(name);
        transport.setIOStrategy(ioStrategy);
        if (workerConfig != null) {
            transport.setWorkerThreadPoolConfig(workerConfig.copy());
        }
        if (kernelConfig != null) {
            transport.setKernelThreadPoolConfig(kernelConfig.copy());
        }
        transport.setSelectorProvider(selectorProvider);
        transport.setSelectorHandler(selectorHandler);
        transport.setMemoryManager(memoryManager);
        transport.setAttributeBuilder(attributeBuilder);
        transport.setSelectorRunnersCount(selectorRunnerCount);
        transport.setNIOChannelDistributor(nioChannelDistributor);
        transport.setFilterChain(filterChain != null ? filterChain : new DefaultFilterChain());
        transport.setClientSocketSoTimeout(clientSocketSoTimeout);
        transport.setConnectionTimeout(connectionTimeout);
        transport.setBlockingReadTimeout(blockingReadTimeout, TimeUnit.MILLISECONDS);
        transport.setBlockingWriteTimeout(blockingWriteTimeout, TimeUnit.MILLISECONDS);
        transport.setReadBufferSize(readBufferSize);
        transport.setWriteBufferSize(writeBufferSize);
        transport.setReuseAddress(reuseAddress);
        transport.setOptimizedForMultiplexing(optimizedForMultiplexing);
        transport.getAsyncQueueWriter()
                    .setMaxPendingBytesPerConnection(maxPendingBytesPerConnection);
        return transport;
    }


    // ------------------------------------------------------- Protected Methods

    /**
     * See: <a href="http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ205">http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ205</a>
     */
    protected abstract T getThis();

    protected abstract NIOTransport create(String name);


    // --------------------------------------------------------- Private Methods


    /**
     * @return the default number of {@link org.glassfish.grizzly.nio.SelectorRunner}s
     *  that should be used.
     */
    private int getRunnerCount() {
        return Runtime.getRuntime().availableProcessors();
    }

}
