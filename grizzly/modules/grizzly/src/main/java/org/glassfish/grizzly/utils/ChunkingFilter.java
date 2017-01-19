/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.grizzly.utils;

import java.io.IOException;
import java.util.logging.Logger;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.glassfish.grizzly.memory.Buffers;


/**
 * The Filter is responsible to break the incoming/outgoing data into chunks and
 * pass them down/up by the {@link FilterChain}.
 * This Filter could be useful for testing reasons to check if all Filters in
 * the {@link FilterChain} work properly with chunked data.
 * 
 * @author Alexey Stashok
 */
public class ChunkingFilter extends BaseFilter {
    private static final Logger LOGGER = Grizzly.logger(ChunkingFilter.class);

    private final int chunkSize;

    /**
     * Construct a <tt>ChunkFilter</tt>, which will break incoming/outgoing data
     * into chunks of the specified size.
     *
     * @param chunkSize the chunk size.
     */
    public ChunkingFilter(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    @Override
    public NextAction handleRead(FilterChainContext ctx) throws IOException {
        return chunk(ctx);
    }


    @Override
    public NextAction handleWrite(FilterChainContext ctx) throws IOException {
        return chunk(ctx);
    }

    private NextAction chunk(FilterChainContext ctx) {
        final Buffer input = ctx.getMessage();
        
        if (!input.hasRemaining()) {
            input.tryDispose();
            return ctx.getStopAction();
        }
        
        final int chunkSizeLocal = Math.min(chunkSize, input.remaining());

        final int oldInputPos = input.position();
        final int oldInputLimit = input.limit();

        Buffers.setPositionLimit(input, oldInputPos, oldInputPos + chunkSizeLocal);

        final Buffer output = ctx.getMemoryManager().allocate(chunkSizeLocal);
        output.put(input).flip();

        Buffers.setPositionLimit(input, oldInputPos + chunkSizeLocal, oldInputLimit);

        ctx.setMessage(output);
        
        return ctx.getInvokeAction(input);
    }
}
