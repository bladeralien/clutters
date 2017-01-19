/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.grizzly.asyncqueue;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.WritableMessage;

/**
 * Callback handler, which will be called by Grizzly {@link org.glassfish.grizzly.Writer}
 * implementation, during the message write process execution.
 * 
 * @since 2.3
 * 
 * @author Alexey Stashok
 */
public interface LifeCycleHandler {
    
    /**
     * This method is invoked, when message write process can not be completed
     * in this thread (thread which initialized write process).
     * 
     * The <tt>LifeCycleHandler</tt> may decide to clone the original message and
     * return it to the writer, so the original message might be immediately reused.
     * Or it may decide to return the original message and writer will keep working
     * with it.
     * 
     * @param connection {@link Connection}
     * @param message {@link WritableMessage}
     * 
     * @return the message, which writer will write asynchronously.
     */
    public WritableMessage onThreadContextSwitch(Connection connection, WritableMessage message);

    /**
     * This method is invoked, when message is about to be written to the channel.
     * 
     * @param connection {@link Connection}
     * @param message {@link WritableMessage}
     */
    public void onBeforeWrite(Connection connection, WritableMessage message);
    
    /**
     * Empty {@link LifeCycleHandler} implementation.
     */
    public static class Adapter
            implements LifeCycleHandler {

        /**
         * {@inheritDoc}
         */
        @Override
        public WritableMessage onThreadContextSwitch(final Connection connection,
                final WritableMessage message) {
            return message;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onBeforeWrite(final Connection connection,
                final WritableMessage message) {
        }
    }
}
