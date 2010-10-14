import com.sapienter.jbilling.server.user.permisson.db.RoleDTO;
import com.sapienter.jbilling.server.user.db.UserStatusDTO;
import com.sapienter.jbilling.server.user.db.SubscriberStatusDTO;

class SelectionTagLib {
	
	def accountType = { attrs, bodyy -> 
		println System.currentTimeMillis();
		String checking, savings;
		savings=checking= ""
		println "taglib: accountType=" + attrs.value + " " + attrs.name
		
		if (attrs.value == 1 ) {
			checking= "checked=checked" 
		} else if (attrs.value == 2 ){
			savings="checked=checked"
		}
		
		out << "Checking<input type='radio' name=\'" + attrs.name + "\' value=\'1\' " + checking + ">"
		out << "Savings<input type='radio' name=\'"  + attrs.name + "\' value=\'2\' "  + savings + ">"
		
	}
	
	def selectRoles = { attrs, body ->
		
		def langId= Integer.parseInt(attrs.languageId);
		def name= attrs.name;		
		def value = attrs.value ? Integer.parseInt(attrs.value): -1
		
		List list= new ArrayList();
		String[] sarr= null;
		boolean selected= false;
		for (RoleDTO role: RoleDTO.list()) {
			String title= role.getTitle(Integer.valueOf(langId));
			sarr=new String[2]
			sarr[0]= role.getId()
			sarr[1]= title
			list.add(sarr)
			if (!selected) {
				selected= (value==sarr[0]) ? true:false
			}
		}
		out << render(template:"/selectTag", model:[name:name, list:list, selected:selected])
	}

	def userStatus = { attrs, body ->
		
		def langId= Integer.parseInt(attrs.languageId);
		def name= attrs.name;
		def value = attrs.value ? (attrs.value instanceof String ? Integer.parseInt(attrs.value): attrs.value) : -1		

		List list= new ArrayList();
		String[] sarr= null;
		boolean selected= false;
		for (UserStatusDTO status: UserStatusDTO.list()) {
			String title= status.getDescription(Integer.valueOf(langId));			
			sarr=new String[2]
			sarr[0]= status.getId()
			sarr[1]= title
			list.add(sarr)
			if (!selected) {
				selected= (value==sarr[0]) ? true:false
			}
		}
		out << render(template:"/selectTag", model:[name:name, list:list, selected:selected])
		
	}
	
	def subscriberStatus = { attrs, body ->
		
		def langId= Integer.parseInt(attrs.languageId);
		def name= attrs.name;
		def value = attrs.value ? (attrs.value instanceof String ? Integer.parseInt(attrs.value): attrs.value) : -1

		List list= new ArrayList();
		String[] sarr= null;
		boolean selected= false;
		for (SubscriberStatusDTO status: SubscriberStatusDTO.list()) {
			String title= status.getDescription(Integer.valueOf(langId));
			sarr=new String[2]
			sarr[0]= status.getId()
			sarr[1]= title
			list.add(sarr)
			if (!selected) {
				selected= (value==sarr[0]) ? true:false
			}			
		}
		
		out << render(template:"/selectTag", model:[name:name, list:list, selected:selected])
	}

					
}