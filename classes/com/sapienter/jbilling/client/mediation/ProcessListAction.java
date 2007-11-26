package com.sapienter.jbilling.client.mediation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.server.mediation.MediationSession;
import com.sapienter.jbilling.server.mediation.MediationSessionHome;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;

public class ProcessListAction extends Action {
    private static final Logger LOG = Logger.getLogger(ProcessListAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            MediationSessionHome mediationHome =
                    (MediationSessionHome) EJBFactory.lookUpHome(
                    MediationSessionHome.class,
                    MediationSessionHome.JNDI_NAME);
            MediationSession mediationSession = mediationHome.create();
        
            HttpSession session = request.getSession(false);
            List <MediationProcess> list = mediationSession.getAll( (Integer)
                    session.getAttribute(Constants.SESSION_ENTITY_ID_KEY));
            
            session.setAttribute("mediation_process_list", list);
                    
            return mapping.findForward("view");            
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }

}
