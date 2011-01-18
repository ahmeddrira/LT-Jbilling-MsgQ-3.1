<select name="${name}" id="${name}" class="${cssClass}">
	<g:each var="sarr" in="${list}" status="idx">
		<g:if test="${value == sarr[0]}">
			<option selected value="${sarr[0]}">${sarr[1]}</option>
		</g:if>
		<g:else>
			<option value="${sarr[0]}">${sarr[1]}</option>
		</g:else>
	</g:each>
</select>
<!--  Created select tag with name ${name } and selected value ${value }  -->
<!-- <option selected value="id">description</option> -->