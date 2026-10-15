<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login"/>
<%@ include file="common/header.jsp" %>

<div class="card narrow">
    <h2>Login</h2>
    <form action="${ctx}/login" method="post">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${email}'/>" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Login</button>
    </form>
    <p class="muted">Don't have an account? <a href="${ctx}/register">Register</a></p>
</div>

<%@ include file="common/footer.jsp" %>
