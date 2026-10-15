<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Register"/>
<%@ include file="common/header.jsp" %>

<div class="card narrow">
    <h2>Create Account</h2>
    <form action="${ctx}/register" method="post">
        <label for="fullName">Full Name</label>
        <input type="text" id="fullName" name="fullName" value="<c:out value='${fullName}'/>" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${email}'/>" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" minlength="6" required>

        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" name="confirmPassword" minlength="6" required>

        <button type="submit">Register</button>
    </form>
    <p class="muted">Already have an account? <a href="${ctx}/login">Login</a></p>
</div>

<%@ include file="common/footer.jsp" %>
