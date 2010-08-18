package src

import com.sapienter.jbilling.server.user.UserDTOEx
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.Constants

class LoginController {

    def userSession

    def index = {
        def staticTitle =  "Welcome to jBilling 3!"
        [title: staticTitle]
        redirect(uri:"/login/login.gsp");
    }
    

    def login = {
        UserDTOEx user = new UserDTOEx();
        user.setUserName(params.userName);
        user.setPassword(params.password);
        user.setCompany(new CompanyDTO(Integer.valueOf(params.entityId)));

        Integer result = userSession.authenticate(user);
        if (result.equals(Constants.AUTH_OK)) {
            render "Login done"
        } else {
            render "Login failed"
        }
    }
}
