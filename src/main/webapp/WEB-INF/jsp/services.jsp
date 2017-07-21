<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 21/02/2017
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp"/>
<jsp:include page="navbar.jsp"/>
<jsp:include page="banner.jsp"/>
    <div id="container">
        <div id="banner">
            <div class="image-border">
                <a href="#">
                    <img src="${WEBROOT}/resources/image/Depositphotos_1981846_original.jpg" width="870" height="253" alt="" class="img-responsive" />
                </a>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading" style = " background: #353535; color: #AFAFAF;">
                <h3 class="panel-title">
                    <spring:message code="services.welcomeTitle"/>
                </h3>
            </div>
            <div class="panel-body">
                <spring:message code="services.introMessage"/>
                <div class="col-md-4">
                    <label>
                        <spring:message code="services.insuranceLabel"/>
                    </label>
                    <select class="form-control" id="insurances" name="insurances"  >
                        <option value=" ">
                    </select>
                    <br/>
                    <label>
                        <spring:message code="services.defaultAmountLabel"/>
                    </label>
                    <div class="input-group">
                        <input class="form-control" id="default_amount" readonly>
                        <span class="input-group-addon">&euro;</span>
                    </div>
                    <br/>
                    <label>
                        <spring:message code="services.repairAmountLabel"/>
                    </label>
                    <div class="input-group">
                        <input class="form-control" type="text" id="amount">
                        <span class="input-group-addon">&euro;</span>
                    </div>
                    <br/>
                    <label>
                        <spring:message code="services.deductibleLabel"/>
                    </label>
                    <div class="input-group">
                        <input class="form-control" type="text" id="deductible" value=" " readonly>
                        <span class="input-group-addon">&euro;</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript" defer>
        const API_ACCESS_KEY = "${API_ACCESS_KEY}";
        const PUBLIC_API_URI = "/public";
    </script>
    <script type="text/javascript" src="${WEBROOT}/resources/script/app/services.js" defer></script>
<jsp:include page="footer.jsp"/>