<%@ page import="com.sapienter.jbilling.server.util.Util"%>

<div class="heading table-heading">
    <strong class="first"><g:message code="label.gui.date"/></strong>
    <strong class="name" style="width:20%"><g:message code="invoice.label.duedate"/></strong>
    <strong class="name" style="width:10%"><g:message code="invoice.label.id"/></strong>
    <strong class="name" style="width:10%"><g:message code="#"/></strong>
    <strong class="name" style="width:10%"><g:message code="invoice.label.status"/></strong>
    <strong class="name" style="width:15%"><g:message code="invoice.label.amount"/></strong>
    <strong class="name" style="width:20%"><g:message code="invoice.label.balance"/></strong>
</div>

<div class="table-box">
	<ul>
		<g:each var="inv" in="${invoices}">
			<li>
				<g:remoteLink action="show" id="${inv.id}" params="[userId: userId]"
		     			before="register(this);" onSuccess="render(data, next);">
				<strong>
				${Util.formatDate(inv.createDateTime, session["user_id"]) }
				</strong>
				<strong>
				${Util.formatDate(inv.dueDate, session["user_id"]) }
				</strong>
				<strong>
				${inv.id }
				</strong>
				<strong>
				${inv.number }
				</strong>
				<strong>
				${inv.statusDescr }
				</strong>
				<strong>
				${Util.formatMoney(new BigDecimal(inv.total),
					session["user_id"],inv.currencyId, false)}
				</strong>
				<span class="block">
					<span>
				${Util.formatMoney(new BigDecimal(inv.balance),
					session["user_id"],inv.currencyId, false)}
				</span></span>
				</g:remoteLink>
			</li>
		</g:each>
	</ul>
</div>

<!-- 
<div class="btn-box">
    <a href="#" class="submit delete">
    	<span><g:message code="button.delete"/></span></a>
</div>
 -->
