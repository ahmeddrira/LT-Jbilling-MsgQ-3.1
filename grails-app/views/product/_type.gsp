
<script type="text/javascript">

	$(document).ready(function() {
	
		$('#catTbl li').click(function () {
			var productId = $(this).find("#productId").val();
			//alert( productId);
			$("#selectedId").val(productId);
		});		
	

		$('#catTbl li').dblclick(function() {	
			//alert(document.getElementById("selectedId").value);
			//$("#productTypes"). att r( " action "," http://google.com ");
			//$("#productTypes").submit();
			document.forms[0].action='/jbilling/product/edit/' + $("#selectedId").val();
			document.forms[0].submit();
		});

	});

	function del() {
		if (0 == parseInt(document.getElementById("selectedId").value))
		{
			alert('Please select a row to delete');
			return false;
		}
		if (confirm("Are you sure you want to delete [" + document.getElementById("selectedId").value + "]")){		
			return true;
		}
		return false;
	}
	function add() {
	}
</script>

<div class="heading table-heading">
	<strong style="width: 100%">
		<g:message code="prompt.products"/>
	</strong>
</div>

<g:form id="productTypes">
	<g:hiddenField name="recCnt" value="0" />
	<g:hiddenField name="selectedId" value="0" />

	<div class="heading table-heading">
	    <strong class="first"><g:message code="product.name"/></strong>
	    <strong style="width:20%"><g:message code="product.internal.number"/></strong>
	    <strong style="width:15%"><g:message code="product.id"/></strong>
	</div>

	<div id="catTbl" class="table-box">
	<ul>
		<g:each in="${list}" status="idx" var="item">
			<!-- TODO The below condition must be superflous -->
			<g:if test="${item.deleted == 0 }">
				<li>
				<g:remoteLink controller="product" action="show" id="${item.id}" 
			     				before="register(this);" onSuccess="render(data, next);">			     
					<strong> <g:textField size="35" readonly="readonly"
						name="item[${idx}].description" value="${item.description}" /> </strong>
					<strong> <g:textField size="4" id="productId"
						readonly="readonly" name="item[${idx}].id" value="${item.id}" />
					</strong>
					<strong> <g:textField size="8" readonly="readonly"
						name="item[${idx}].internalNumber" value="${item.internalNumber}" />
					</strong>
				</g:remoteLink></li>
			</g:if>
		</g:each>
	</ul>
	</div>

	<div class="btn-box">
	    <a href="${createLink(action: 'add')}" class="submit add">
	    	<span><g:message code="button.add.item"/></span></a>
	   	<a href="${createLink(action: 'del')}" onclick="javascript: return del()" class="submit cancel">
	    	<span><g:message code="button.delete"/></span></a>
	    <a href="${createLink(action: 'showAll')}" class="submit">
	    	<span><g:message code="button.show.all"/></span></a>
	</div>

</g:form>

