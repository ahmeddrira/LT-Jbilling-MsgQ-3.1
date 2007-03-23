package com.sapienter.jbilling.server;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

public abstract class WSTestBase extends TestCase {
	
	protected String getEndpoint(){
		return "http://localhost/jboss-net/services/billing";
	}
	
	protected Call createTestCall() throws Exception {
        /* If using https, you need an ssh key. You can configure ANT to
         * pass on the java properties like this:
         * export ANT_OPTS="-Djavax.net.ssl.trustStore=c:\\\\sapienter\\\\ssl\\\\client.keystore -Djavax.net.ssl.trustStorePassword=pass"
         */
        Service  service = new Service();
        Call  call = (Call) service.createCall();
        call.setTargetEndpointAddress(getEndpointURL());
        call.setUsername("admin");
        call.setPassword("asdfasdf");
        
        return call;
	}
	
	protected final void addBeanTypeMapping(Call call, Class<?> beanClass){
        QName qn = new QName("http://www.sapienter.com/billing", beanClass.getSimpleName());
        BeanSerializerFactory ser1 = new BeanSerializerFactory(beanClass, qn);
        BeanDeserializerFactory ser2 = new BeanDeserializerFactory (beanClass, qn);
        call.registerTypeMapping(beanClass, qn, ser1, ser2); 
	}
	
	protected final URL getEndpointURL() throws MalformedURLException {
		return new URL(getEndpoint());
	}

}
