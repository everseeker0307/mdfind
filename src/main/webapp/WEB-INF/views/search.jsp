<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: everseeker
  Date: 16/9/12
  Time: 上午9:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search</title>
</head>
<body>
<div id="search_div" align="center" style="margin-top: 50px">
    <form method="post">
        <input type="text" name="keyword" size="60" />
        <input type="submit" value="搜索" />
    </form>
</div>
<div id="id1">
    <c:forEach items="${keyFileList}" var="keyFile">
        <hr />
        <p><h4><c:out value="${keyFile.key}" /></h4></p>
        <div>
            <c:forEach items="${keyFile.value}" var="fileContent">
                <c:out value="${fileContent.key}" />
                <p style="font-size: 80%"><c:out value="${fileContent.value}" /></p>
            </c:forEach>
        </div>
    </c:forEach>
</div>
</body>
<%-- 以下脚本负责将关键字keyword标红显示 --%>
<script type="text/javascript">
    function changeColor(key) {
        if (key !== undefined) {
            let content = document.getElementById('id1').innerHTML;
            let regExp = new RegExp(key, 'g');
            content = content.replace(regExp, '<span style="font-weight:bold; color:red">' + key + '</span>');
            document.getElementById('id1').innerHTML = content;
            console.log(key);
        }
    }
    window.onload = changeColor("${keyword}");
</script>
</html>
