/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.grizzly.samples.jaxws.addclient;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "AddServiceService", targetNamespace = "http://service.jaxws.samples.grizzly.glassfish.org/", wsdlLocation = "http://localhost:19881/add?wsdl")
public class AddServiceService
    extends Service
{

    private final static URL ADDSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException ADDSERVICESERVICE_EXCEPTION;
    private final static QName ADDSERVICESERVICE_QNAME = new QName("http://service.jaxws.samples.grizzly.glassfish.org/", "AddServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:19881/add?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ADDSERVICESERVICE_WSDL_LOCATION = url;
        ADDSERVICESERVICE_EXCEPTION = e;
    }

    public AddServiceService() {
        super(__getWsdlLocation(), ADDSERVICESERVICE_QNAME);
    }

    public AddServiceService(URL wsdlLocation) {
        super(wsdlLocation, ADDSERVICESERVICE_QNAME);
    }

    public AddServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns AddService
     */
    @WebEndpoint(name = "AddServicePort")
    public AddService getAddServicePort() {
        return super.getPort(new QName("http://service.jaxws.samples.grizzly.glassfish.org/", "AddServicePort"), AddService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AddService
     */
    @WebEndpoint(name = "AddServicePort")
    public AddService getAddServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.jaxws.samples.grizzly.glassfish.org/", "AddServicePort"), AddService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ADDSERVICESERVICE_EXCEPTION!= null) {
            throw ADDSERVICESERVICE_EXCEPTION;
        }
        return ADDSERVICESERVICE_WSDL_LOCATION;
    }

}
