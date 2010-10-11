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
		def value = attrs.value ? Integer.parseInt(attrs.value): -1		

		StringBuffer sb= new StringBuffer("");
		for (UserStatusDTO status: UserStatusDTO.list()) {
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
	
	def subscriberStatus = { attrs, body ->
		
		def langId= Integer.parseInt(attrs.languageId);
		def name= attrs.name;
		def value = attrs.value ? Integer.parseInt(attrs.value): -1

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