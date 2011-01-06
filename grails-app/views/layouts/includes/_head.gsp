<%--
  Content for the head region of all jBilling layouts.

  @author Brian Cowdery
  @since  23-11-2010
--%>

<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<title><g:layoutTitle default="jBilling" /></title>

<link rel="shortcut icon" href="${resource(dir:'images', file:'favicon.ico')}" type="image/x-icon" />

<link media="all" rel="stylesheet" href="${resource(dir:'css', file:'all.css')}" type="text/css" />
<!--[if lt IE 8]><link rel="stylesheet" href="${resource(dir:'css', file:'lt7.css')}" type="text/css" media="screen"/><![endif]-->

<g:javascript library="jquery" plugin="jquery"/>
<jqui:resources theme="smoothness"/>

<script type="text/javascript">
    function renderMessages() {
        $.ajax({
            url: "${resource(dir:'')}/messages",
            global: false,
            async: false,
            success: function(data) { $("#messages").replaceWith(data); }
        });
    }

    function renderBreadcrumbs() {
        $.ajax({
            url: "${resource(dir:'')}/breadcrumb",
            global: false,
            success: function(data) { $("#breadcrumbs").replaceWith(data); }
       });
    }

    $(document).ajaxSuccess(function(e, xhr, settings) {
        renderMessages();
        renderBreadcrumbs();
    });
    $(document).ajaxError(function(e, xhr, settings) {
        renderMessages();
    });
</script>

<g:javascript library="clearinput"/>
<g:javascript library="slideBlock"/>
<g:javascript library="main"/>

%{-- disable form style replacement javascript --%}
%{--
<g:javascript library="form"/>
<g:javascript library="checkbox"/>
--}%

