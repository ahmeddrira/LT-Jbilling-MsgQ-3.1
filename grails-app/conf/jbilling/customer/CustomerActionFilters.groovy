package jbilling.customer

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.apache.log4j.Logger

class CustomerActionFilters {

    private static def LOG = Logger.getLogger(this)

    def springSecurityService

    def filters = {
        validateCustomerAction(controller: 'customer', action: '*') {
            before = {
                List<String> actionsExcluded = ['list', 'index', 'save']
                LOG.debug "user id is " + springSecurityService.principal.id
                LOG.debug "params.id is " + params.id
                if (params.action in actionsExcluded) {
                    LOG.debug "${params.action} is excluded, so executing the action"
                    return true
                }
                else if ((springSecurityService.isLoggedIn() && springSecurityService.principal.id == params.id as int) || SpringSecurityUtils.ifAllGranted('MENU_90')) {
                    LOG.debug "execute action " + params.action
                    return true
                }
                else if(!params.action) {
                    LOG.debug "No action param"
                    return true
                }
                else {
                    LOG.debug "do not execute action " + params.action
                    redirect(controller: 'login',action: 'denied')
                    return false
                }
            }
            after = {

            }
            afterView = {

            }
        }
    }

}
