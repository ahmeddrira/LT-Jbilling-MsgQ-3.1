<%--
  messages

  @author Brian Cowdery
  @since  23-11-2010
--%>

<%-- show flash messages if available --%>
<%-- either 'flash.message', 'flash.info', 'flash.warn' or 'flash.error' --%>
<g:if test='${flash.message}'>
    <div class="msg-box successfully">
        <img src="${resource(dir:'images', file:'icon20.gif')}" alt="${message(code:'success.icon.alt',default:'Success')}"/>
        <strong><g:message code="flash.success.title"/></strong>
        <p>${flash.message}</p>
    </div>
</g:if>
<g:if test='${flash.info}'>
    <div class="msg-box successfully">
        <img src="${resource(dir:'images', file:'icon34.gif')}" alt="${message(code:'info.icon.alt',default:'Information')}"/>
        <strong><g:message code="flash.info.title"/></strong>
        <p>${flash.info}</p>
    </div>
</g:if>
<g:if test='${flash.warn}'>
    <div class="msg-box successfully">
        <img src="${resource(dir:'images', file:'icon32.gif')}" alt="${message(code:'warn.icon.alt',default:'Warning')}"/>
        <strong><g:message code="flash.warn.title"/></strong>
        <p>${flash.warn}</p>
    </div>
</g:if>
<g:if test='${flash.error}'>
    <div class="msg-box error">
        <img src="${resource(dir:'images', file:'icon14.gif')}" alt="${message(code:'error.icon.alt',default:'Error')}"/>
        <strong><g:message code="flash.error.title"/></strong>
        <p>${flash.error}</p>
    </div>
</g:if>

