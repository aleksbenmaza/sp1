<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 20/02/2017
  Time: 13:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="f" uri="http://tagutils"%>
<c:set var="requestURI" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<spring:eval var="routes" expression="@routes"/>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand">
                <img src="${WEBROOT}/resources/image/aaa.png" class="img-responsive" style="margin-top: -0.4rem;" width="50px" height="60px"/>
            </a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="${requestURI eq routes['public.home'] ? 'active' : '' }">
                    <a href="${WEBROOT}${routes['public.home']}" accesskey="1" title="">
                        <span>
                            <spring:message code="navbar.titles.home"/>
                        </span>
                    </a>
                </li>
                <li class="${requestURI eq routes['public.services'] ? 'active' : '' }">
                    <a href="${WEBROOT}${routes['public.services']}" accesskey="2" title="">
                        <span>
                            <spring:message code="navbar.titles.services"/>
                        </span>
                    </a>
                </li>
                <li class="${requestURI eq routes['public.press'] ? 'active' : '' }">
                    <a href="${WEBROOT}${routes['public.press']}" accesskey="4" title="">
                        <span>
                            <spring:message code="navbar.titles.press"/>
                        </span>
                    </a>
                </li>
                <li class="${requestURI eq routes['public.about'] ? 'active' : '' }">
                    <a href="${WEBROOT}${routes['public.about']}" accesskey="3" title="">
                        <span>
                            <spring:message code="navbar.titles.about"/>
                        </span>
                    </a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${f:isRegistered(user)}">
                        <li>
                            <c:if test="${f:isCustomer(user)}">
                                <a href="${routes['customerPanel.root']}">
                                    <span>
                                        ${f:getShortenedFullName(user)}
                                    </span>
                                </a>
                            </c:if>
                            <c:if test="${f:isManager(user)}">
                                <a href="${routes['managerPanel.root']}">
                                    <span>
                                        <spring:message code="navbar.titles.managerPanel"/>
                                    </span>
                                </a>
                            </c:if>
                        </li>
                        <li role="presentation">
                            <form method="post" action="${WEBROOT}${routes.logout}">
                                <button type="submit" class="btn btn-link btn-logout">
                                    <span>
                                        <spring:message code="navbar.titles.logout"/>
                                    </span>
                                </button>
                            </form>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li role="presentation">
                            <a href="${routes.login}">
                                <span>
                                    <spring:message code="navbar.titles.login"/>
                                </span>
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>