<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <title><g:message code="login.page.title"/></title>

    <link rel="shortcut icon" href="${resource(dir:'images', file:'favicon.ico')}" type="image/x-icon" />

    <link media="all" rel="stylesheet" href="${resource(dir:'css', file:'all.css')}" type="text/css" />
    <!--[if lt IE 8]><link rel="stylesheet" href="${resource(dir:'css', file:'lt7.css')}" type="text/css" media="screen"/><![endif]-->

    <g:javascript library="jquery" plugin="jquery"/>
    <g:javascript library="main" />

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
                <strong><g:message code="flash.error.title"/></strong>
                <p>${flash.message}</p>
            </div>
        </g:if>
        
        <div id="login">
            <div class="form-box">
                <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                    <fieldset>

                        <div class="input-row">
                            <label for='username'><g:message code="login.prompt.username"/></label>
                            <div class="input-bg">
                                <input type='text' class='text' name='j_username' id='username'/>
                            </div>
                        </div>

                        <div class="input-row">
                            <label for='password'><g:message code="login.prompt.password"/></label>
                            <div class="input-bg">
                                <input type='password' class='text' name='j_password' id='password'/>
                            </div>
                        </div>

                        <div class="input-row">
                            <label for='client_id'><g:message code="login.prompt.client.id"/></label>
                            <div class="input-bg">
                                <input type='text' class='text' name='j_client_id' id='client_id'/>
                            </div>
                        </div>

                        <br/>
                        <div class="input-row">
                            <label for='remember_me'><g:message code="login.prompt.remember.me"/></label>
                            <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if> />
                        </div>

                        <br/>
                        <div class="input-row center">
                            <a href="#" class="submit order" onclick="$('#login form').submit();"><span><g:message code="login.button.submit"/></span></a>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>