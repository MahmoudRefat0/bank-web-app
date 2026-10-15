<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Withdraw"/>
<%@ include file="common/header.jsp" %>

<div class="card narrow">
    <h2>Withdraw</h2>
    <form action="${ctx}/withdraw" method="post">
        <label for="accountId">Account</label>
        <select id="accountId" name="accountId" required>
            <c:forEach var="a" items="${accounts}">
                <c:if test="${a.active}">
                    <option value="${a.id}" ${a.id == selectedId ? 'selected' : ''}>
                        ${a.accountNumber} (<fmt:formatNumber value="${a.balance}" minFractionDigits="2" maxFractionDigits="2"/>)
                    </option>
                </c:if>
            </c:forEach>
        </select>

        <label for="amount">Amount</label>
        <input type="number" id="amount" name="amount" min="0.01" step="0.01" required>

        <button type="submit">Withdraw</button>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
