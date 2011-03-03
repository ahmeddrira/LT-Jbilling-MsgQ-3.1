<%--
  _breadcrumbs

  @author Brian Cowdery
  @since  23-11-2010
--%>

<!-- breadcrumbs -->
<div id="breadcrumbs" class="breadcrumbs">
    <div id="spinner" style="display: none;">
        <img src="${resource(dir:'images', file:'spinner.gif')}" alt="loading..." />
    </div>

    <script type="text/javascript">
        $('#spinner').ajaxStart(function() { $(this).show('fade'); });
        $('#spinner').ajaxStop(function() { $(this).hide('fade'); })
    </script>

    <ul>
        <g:each var="crumb" in="${session['breadcrumbs']}">
            <li>
                <g:link controller="${crumb.controller}" action="${crumb.action}" id="${crumb.objectId}">
                    <g:message code="${crumb.messageCode}" args="[crumb.objectId]"/>
                </g:link>
            </li>
        </g:each>
        <li style="width:135px;
                text-align:center;
                color:#fff;
                position:relative;
                font-weight:bold;
                float:left;"> 
            <g:remoteLink controller="shortcut" action="add" params="['template': 'shortcuts']" class="shortcut">
                <span><g:message code="shortcut.add"/></span>
            </g:remoteLink>
        </li>
    </ul>
</div>
