<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Error"/>
<%@ include file="common/header.jsp" %>

<div class="card narrow">
    <h2>Oops!</h2>
    <p>The page you requested is not available or something went wrong.</p>
    <a class="button" href="${ctx}/dashboard">Go to dashboard</a>
</div>

<%@ include file="common/footer.jsp" %>
