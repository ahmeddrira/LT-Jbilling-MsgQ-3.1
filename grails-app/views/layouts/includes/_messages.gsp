<%--
  messages

  @author Brian Cowdery
  @since  23-11-2010
--%>

<%-- show flash messages if available --%>
<%-- either 'flash.message', 'flash.info', 'flash.warn' or 'flash.error' --%>
<%-- will also print all messages from 'flash.errorMessages' as an unordered list --%>
<div id="messages">
    <g:if test='${flash.message}'>
        <div class="msg-box successfully">
            <img src="${resource(dir:'images', file:'icon20.gif')}" alt="${message(code:'success.icon.alt',default:'Success')}"/>
            <strong><g:message code="flash.success.title"/></strong>
            <p><g:message code="${flash.message}" args="${flash.args}"/></p>
        </div>
    </g:if>
    <g:if test='${flash.info}'>
        <div class="msg-box info">
            <img src="${resource(dir:'images', file:'icon34.gif')}" alt="${message(code:'info.icon.alt',default:'Information')}"/>
            <strong><g:message code="flash.info.title"/></strong>
            <p><g:message code="${flash.info}" args="${flash.args}"/></p>
        </div>
    </g:if>
    <g:if test='${flash.warn}'>
        <div class="msg-box warn">
            <img src="${resource(dir:'images', file:'icon32.gif')}" alt="${message(code:'warn.icon.alt',default:'Warning')}"/>
            <strong><g:message code="flash.warn.title"/></strong>
            <p><g:message code="${flash.warn}" args="${flash.args}"/></p>
        </div>
    </g:if>
    <g:if test='${flash.error}'>
        <div class="msg-box error">
            <img src="${resource(dir:'images', file:'icon14.gif')}" alt="${message(code:'error.icon.alt',default:'Error')}"/>
            <strong><g:message code="flash.error.title"/></strong>
            <p><g:message code="${flash.error}" args="${flash.args}"/></p>
        </div>
    </g:if>
    <g:if test="${flash.errorMessages}">
        <div class="msg-box error">
            <img src="${resource(dir:'images', file:'icon14.gif')}" alt="${message(code:'error.icon.alt',default:'Error')}"/>
            <strong><g:message code="flash.validation.error.title"/></strong>
            <ul>
                <g:each var="message" in="${flash.errorMessages}">
                    <li>${message}</li>
                </g:each>
            </ul>
        </div>
    </g:if>
</div>

<script type="text/javascript">
    $('#messages').ajaxSuccess(function(e, xhr, settings) {
        var url = "${resource(dir:'')}/messages";
        if (settings.url != url) {
            $.ajax({
                url: url,
                success: function(data) {
                    $('#messages').replaceWith(data);
                }
            });
        }
    });
</script>

