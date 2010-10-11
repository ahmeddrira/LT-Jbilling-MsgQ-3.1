<select name="${name}" id="${name}">
	<g:each var="sarr" in="${list}">
		<g:if test="${selected}">
			<option selected value="${sarr[0]}">${sarr[1]}</option>
		</g:if>
		<g:else>
			<option value="${sarr[0]}">${sarr[1]}</option>
		</g:else>
	</g:each>
</select>
<!-- <option selected value="id">description</option> -->