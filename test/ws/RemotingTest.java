import junit.framework.TestCase;

import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.util.RemoteContext;
import java.net.URL;
import javax.xml.ws.Service;
import javax.xml.namespace.QName;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;



public class RemotingTest extends TestCase {

	private IWebServicesSessionBean service = null;
	
	public void testHessian() {

		service = (IWebServicesSessionBean) RemoteContext.getBean(RemoteContext.Name.API_CLIENT);
		
		System.out.println("Hessian tests");
		makeCalls();
		System.out.println("Hessian tests done");
	}
	
	public void testWebServices() {

		service = (IWebServicesSessionBean) RemoteContext.getBean(RemoteContext.Name.API_CLIENT_2);
		
		System.out.println("Web Services tests");
		makeCalls();
		System.out.println("Web Services tests done");
	}

	public void testInvoker() {

		service = (IWebServicesSessionBean) RemoteContext.getBean(RemoteContext.Name.API_CLIENT_3);
		
		System.out.println("HTTP Invoker tests");
		makeCalls();
		System.out.println("HTTP Invoker tests done");
	}

	
	/*
	 * TODO: add here calls and asserts to cover every method of IWebServicesSessionBean
	 */
	private void makeCalls() {
		try {
			// the goal is to test that the call can be done, not to test business logic
			// example: 
			
			// test getInvoiceWS
			InvoiceWS invoice = service.getInvoiceWS(123);
			assertNotNull(invoice);
			assertEquals(123, invoice.getId().intValue());
			
			// test getLatestInvoice
			//...
			
			// the rest of the tests to include every method of IWebServicesSessionBean
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception" + e.getMessage());
		}
	}

}
