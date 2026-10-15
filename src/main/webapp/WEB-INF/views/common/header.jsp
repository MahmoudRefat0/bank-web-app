<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pageTitle} | Bank</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<header class="navbar">
    <a class="brand" href="${ctx}/dashboard">Bank</a>
    <c:if test="${not empty sessionScope.user}">
        <nav>
            <a href="${ctx}/dashboard">Dashboard</a>
            <a href="${ctx}/deposit">Deposit</a>
            <a href="${ctx}/withdraw">Withdraw</a>
            <a href="${ctx}/transfer">Transfer</a>
            <form action="${ctx}/logout" method="post" class="inline">
                <button type="submit" class="link-button">Logout</button>
            </form>
        </nav>
    </c:if>
</header>
<main class="container">
    <c:if test="${not empty success}">
        <div class="alert success"><c:out value="${success}"/></div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert error"><c:out value="${error}"/></div>
        <c:remove var="error" scope="session"/>
    </c:if>
