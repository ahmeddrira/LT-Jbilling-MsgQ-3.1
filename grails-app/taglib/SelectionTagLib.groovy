import com.sapienter.jbilling.server.user.permisson.db.RoleDTO;
import com.sapienter.jbilling.server.user.db.UserStatusDTO;
import com.sapienter.jbilling.server.user.db.SubscriberStatusDTO;

class SelectionTagLib {

	def selectRoles = { attrs, body ->
		
		def langId= Integer.parseInt(attrs.languageId);
		def name= attrs.name;		
		def value = attrs.value ? Integer.parseInt(attrs.value): -1
		
		StringBuffer sb= new StringBuffer("");
		for (RoleDTO role: RoleDTO.list()) {
			String title= role.getTitle(Integer.valueOf(langId));
			sb.append("<option ")
			if ( value == role.getId()) {
				sb.append("selected")
			}
			sb.append(" value=\"").append(role.getId())		
			.append("\" >").append(title).append("</option> ");
		}
		
		out << "<select name=\"" + name + "\" id=\"" + name + "\" >"
		out << "	<option value=\"null\">-</option> "
		out << sb.toString()
		out << "</select>"
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

		StringBuffer sb= new StringBuffer("");
		for (SubscriberStatusDTO status: SubscriberStatusDTO.list()) {
			String title= status.getDescription(Integer.valueOf(langId));
			sb.append("<option ")
			if ( value == status.getId()) {
				sb.append("selected")
			}
			sb.append(" value=\"").append(status.getId())
			.append("\" >").append(title).append("</option> ");
		}
		
		out << "<select name=\"" + name + "\" id=\"" + name + "\" >"
		out << "	<option value=\"null\">-</option> "
		out << sb.toString()
		out << "</select>"
	}

					
}