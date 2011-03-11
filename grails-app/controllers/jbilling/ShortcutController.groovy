package jbilling

class ShortcutController {

	def breadcrumbService

	def index = {
		session['shortcuts'] = getShortcuts().asList()
        render template: "/layouts/includes/shortcuts"
    }

	/**
	 * Returns a list of user's shortcuts.
	 *
	 * @return list of user's Shortcuts
	 */
	def Object getShortcuts() {
		return Shortcut.withCriteria {
			eq("userId", session["user_id"])
			order("id", "asc")
		}
	}

	def add = {
        def crumbs = breadcrumbService.getBreadcrumbs()
		def lastCrumb= !crumbs.isEmpty() ? crumbs.getAt(-1) : null

		if (lastCrumb) {
			def shortcuts= getShortcuts().asList()
			Shortcut shortcut= new Shortcut(controller: lastCrumb.controller, action: lastCrumb.action, name: lastCrumb.name, objectId: lastCrumb.objectId)
			shortcut.userId= session['user_id']
			
			Shortcut exists= shortcuts.find { it == shortcut }
			if (exists) {
				log.debug "${exists.id}"
				flash.info = 'shortcuts.save.exists'
			} else {
				shortcut.save()
				flash.message = 'shortcuts.save.success'
			}
		}
   }
	
	def remove = {
		def shortcuts= getShortcuts().asList()
		Shortcut exists= shortcuts.find { it.id == params.id as Integer}
		if (exists) {
			exists.delete()
			log.info shortcuts.remove(exists) 
			flash.message = 'shortcuts.remove.success'
		}
		log.info shortcuts.size
		session['shortcuts']= shortcuts
		//render template: "/layouts/includes/shortcuts"
	}
}
