package action

import grails.plugins.springsecurity.Secured

class SecureController {

    @Secured(['isAuthenticated()'])
    def index = {
      render view: "index", params: params
    }

    @Secured(['WEB_SERVICES_120'])
    def ws = {
      render 'user has access to web services.'
    }

    @Secured(['ROLE_SUPER_USER'])
    def admin = {
      render 'user is a super user.'
    }

    @Secured(["isAuthenticated() and authentication.name == 'admin'"])
    def expression = {
      render 'Expression evaluated, user is admin.';
    } 
}
