package jbilling

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

/**
 * Filter to capture the last viewed URI in a cookie for future reference. This filter
 * uses the {@link Breadcrumb} system to determine the last viewed page within jbilling.
 *
 * @author Brian Cowdery
 * @since  22-11-2010
 */
class LastViewedFilters {

    public static final String COOKIE_LAST_VIEWED = "last_viewed_uri"

    def filters = {
        all(controller:'*', action:'*') {
            after = {
                def breadcrumb = Breadcrumb.findByUserId(session['user_id'], [sort:'id', order:'desc'])

                if (breadcrumb) {
                    // calculate the uri for the last entered breadcrumb
                    def g = new ApplicationTagLib()
                    def uri = g.createLink(
                            controller: breadcrumb.controller,
                            action: breadcrumb.action,
                            id: breadcrumb.objectId
                    ).toString()

                    // update "last_viewed" uri cookie
                    if (request.getCookie(COOKIE_LAST_VIEWED) != uri) {
                        response.setCookie(COOKIE_LAST_VIEWED, uri, 604800)
                    }
                }
            }
        }
    }

}
