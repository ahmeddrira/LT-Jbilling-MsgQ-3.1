<html>
<head>
    <meta name="layout" content="main" />
</head>
<body>
    <h2>Welcome</h2>
    <div>
        <g:each in="${users}" var="user">
            <p>${user}</p>
        </g:each>
    </div>
</body>
</html>