<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Account ${account.accountNumber}"/>
<%@ include file="common/header.jsp" %>

<div class="card">
    <h2>Account ${account.accountNumber}</h2>
    <p>Type: <strong>${account.accountType}</strong></p>
    <p>Status: <span class="badge ${account.status}">${account.status}</span></p>
    <p class="big"><fmt:formatNumber value="${account.balance}" minFractionDigits="2" maxFractionDigits="2"/></p>

    <c:if test="${account.active}">
        <div class="row">
            <a class="button" href="${ctx}/deposit?accountId=${account.id}">Deposit</a>
            <a class="button" href="${ctx}/withdraw?accountId=${account.id}">Withdraw</a>
            <a class="button" href="${ctx}/transfer?accountId=${account.id}">Transfer</a>
            <form action="${ctx}/accounts/close" method="post" class="inline"
                  onsubmit="return confirm('Are you sure you want to close this account?')">
                <input type="hidden" name="id" value="${account.id}">
                <button type="submit" class="danger">Close Account</button>
            </form>
        </div>
    </c:if>
</div>

<div class="card">
    <h3>Transaction History</h3>
    <c:choose>
        <c:when test="${empty transactions}">
            <p class="muted">No transactions yet.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Balance After</th>
                    <th>Related Account</th>
                </tr>
                <c:forEach var="t" items="${transactions}">
                    <c:set var="isOut" value="${t.type == 'WITHDRAWAL' or t.type == 'TRANSFER_OUT'}"/>
                    <tr>
                        <td>${t.formattedDate}</td>
                        <td>${t.type}</td>
                        <td class="${isOut ? 'out' : 'in'}">
                            ${isOut ? '-' : '+'}<fmt:formatNumber value="${t.amount}" minFractionDigits="2" maxFractionDigits="2"/>
                        </td>
                        <td><fmt:formatNumber value="${t.balanceAfter}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td>${empty t.relatedAccount ? '-' : t.relatedAccount}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<a href="${ctx}/dashboard">Back to dashboard</a>

<%@ include file="common/footer.jsp" %>
