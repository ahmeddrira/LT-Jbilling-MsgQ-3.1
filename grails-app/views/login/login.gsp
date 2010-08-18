<html>
    <head>
        <title>${title}</title>
    </head>
    <body>
        <p>Here goes the form</p>
        <table class="body"> 
            <tr> 
                <td valign="top"> 
                    <p class="title">User Login</p> 

            <g:form action="login">
                        <table class="form"> 
                            <tr class="form"> 
                                <td class="form_prompt"> 
                                    User Name
                                </td> 
                                <td> 
                                    <input type="text" name="userName" maxlength="20" size="20" value=""> 
                                </td> 
                            </tr> 

                            <tr class="form"> 
                                <td class="form_prompt"> 
                                    Password
                                </td> 
                                <td> 
                                    <input type="password" name="password" maxlength="20" size="20" value=""> 
                                </td> 
                            </tr> 


                            <tr class="form"> 
                                <td class="form_prompt"> 
		  Company ID
                                </td> 
                                <td> 
                                    <input type="text" name="entityId" maxlength="4" size="4" value=""> 
                                </td> 
                            </tr> 

                            <tr> 
                                <td colspan="2" class="form_button"> 
                                    <input type="submit" name="submit" value="Submit" class="form_button"> 
                                </td> 
                            </tr> 

                        </table> 

                    </g:form>

                </td> 
            </tr> 
            <tr> 
                <td colspan="3"> 
                    <p class="footer">Visit&nbsp;<a target="jbsite" href="http://www.jbilling.com">jbilling.com</a>&nbsp;for documentation and support.<br/> 
                        Copyright Enterprise jBilling Software Ltd. (c) 2009. All rights reserved.</p> 

                </td> 
            </tr> 
        </table>
    </body>
</html>