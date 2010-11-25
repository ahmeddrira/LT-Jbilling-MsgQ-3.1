<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <title><g:message code="login.page.title"/></title>

    <link rel="shortcut icon" href="${resource(dir:'images', file:'favicon.ico')}" type="image/x-icon" />

    <link media="all" rel="stylesheet" href="${resource(dir:'css', file:'all.css')}" type="text/css" />
    <!--[if lt IE 8]><link rel="stylesheet" href="${resource(dir:'css', file:'lt7.css')}" type="text/css" media="screen"/><![endif]-->

    <g:javascript library="jquery" plugin="jquery"/>
    <g:javascript library="slideBlock" />
    <g:javascript library="clearinput" />
    <g:javascript library="main" />
    <g:javascript library="form" />
    <g:javascript library="checkbox" />

    <script type='text/javascript'>
        $(document).ready(function() {
            $('#login input[name="j_username"]').focus();
        });
    </script>
</head>
<body>
<div id="wrapper">
    <!-- header -->
    <div id="header">
        <h1><a href="${resource(dir:'')}">jBilling</a></h1>
    </div>
    <div id="navigation">
        <ul>
        </ul>
    </div>

    <div id="main">
        <g:if test='${flash.message}'>
            <div class="msg-box login error">
                <img src="${resource(dir:'images', file:'icon14.gif')}" alt="${message(code:'error.icon.alt',default:'Error')}"/>
                <strong><g:message code="flash.error.title" args="${flash.args}"/></strong>
                <p>${flash.message}</p>
            </div>
        </g:if>

        <div id="login" class="form-edit">
            <div class="heading">
                <strong><g:message code="login.prompt.title"/></strong>
            </div>
            <div class="form-hold">
                <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                    <fieldset>

                        <div class="form-columns">

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="login.prompt.username"/></content>
                                <content tag="label.for">username</content>
                                <input type="text" class="field" name="j_username" id="username"/>
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="login.prompt.password"/></content>
                                <content tag="label.for">password</content>
                                <input type="password" class="field" name="j_password" id="password"/>
                            </g:applyLayout>

                            <g:applyLayout name="form/select">
                                <content tag="label"><g:message code="login.prompt.client.id"/></content>
                                <content tag="label.for">client_id</content>
                                <select name="j_client_id" id="client_id">
                                    <g:each var="company" in="${companies}">
                                        <option value="${company.id}">${company.description}</option>
                                    </g:each>
                                </select>
                            </g:applyLayout>

                            <g:applyLayout name="form/checkbox">
                                <content tag="label"><g:message code="login.prompt.remember.me"/></content>
                                <content tag="name">${rememberMeParameter}</content>
                                <content tag="checked">${hasCookie}</content>
                            </g:applyLayout>

                            <br/>

                        </div>
                        <div class="buttons">
                            <ul>
                                <li>
                                    <a href="#" class="submit save" onclick="$('#login form').submit();">
                                        <span><g:message code="login.button.submit"/></span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>