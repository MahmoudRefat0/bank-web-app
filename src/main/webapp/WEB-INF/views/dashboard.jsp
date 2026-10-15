<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Dashboard"/>
<%@ include file="common/header.jsp" %>

<h2>Welcome, <c:out value="${sessionScope.user.fullName}"/></h2>

<div class="card">
    <p class="muted">Total Balance</p>
    <p class="big"><fmt:formatNumber value="${totalBalance}" minFractionDigits="2" maxFractionDigits="2"/></p>
</div>

<div class="card">
    <h3>My Accounts</h3>
    <c:choose>
        <c:when test="${empty accounts}">
            <p class="muted">You don't have any accounts yet.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Account Number</th>
                    <th>Type</th>
                    <th>Balance</th>
                    <th>Status</th>
                    <th></th>
                </tr>
                <c:forEach var="a" items="${accounts}">
                    <tr>
                        <td>${a.accountNumber}</td>
                        <td>${a.accountType}</td>
                        <td><fmt:formatNumber value="${a.balance}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><span class="badge ${a.status}">${a.status}</span></td>
                        <td><a href="${ctx}/accounts/view?id=${a.id}">View</a></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<div class="card">
    <h3>Open New Account</h3>
    <form action="${ctx}/accounts/open" method="post" class="row">
        <select name="accountType">
            <option value="SAVINGS">Savings</option>
            <option value="CURRENT">Current</option>
        </select>
        <button type="submit">Open Account</button>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
