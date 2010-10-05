import com.sapienter.jbilling.server.user.permisson.db.RoleDTO;

class SelectionTagLib {

	def selectRoles = { attrs, body ->
		
		def langId= Integer.parseInt(attrs.languageId);
		def name= attrs.name;
		
		System.out.println("Language Id passed from GSP: " + langId);

		StringBuffer sb= new StringBuffer("");
		for (RoleDTO role: RoleDTO.list()) {
			String title= role.getTitle(Integer.valueOf(langId));
			sb.append("<option value=\"").append(role.getId())
				.append("\" >").append(title).append("</option> ");
		}
		
		out << "<select name=\"" + name + "\" id=\"" + name + "\" >"
		out << "	<option value=\"null\">Please Choose...</option> "
		out << sb.toString()
		out << "</select>"
	}
		
}