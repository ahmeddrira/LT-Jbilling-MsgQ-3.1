package action

import grails.plugins.springsecurity.Secured

class SecureController {

    @Secured(['ROLE_SUPER_USER'])
    def index = {
      render 'Secure access only, user is a super user.'
    }

    @Secured(['WEB_SERVICES_120'])
    def ws = {
      render 'user has access to web services.'
    }

    @Secured(["isAuthenticated() and authentication.name == 'admin'"])
    def expression = {
      render 'Expression evaluated, user is admin.';
    } 
}
