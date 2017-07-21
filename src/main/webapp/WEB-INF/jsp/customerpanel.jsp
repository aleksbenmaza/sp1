<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 04/04/2017
  Time: 19:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>
<%@ include file="navbar.jsp" %>
<%@ include file="banner.jsp" %>
<div id="container" ng-app="CustomerPanel">
    <div id="banner">
        <div class="image-border">
            <a href="#">
                <img src="${WEBROOT}/resources/image/cheap-car-insurance-quotes-4.jpg" width="870" height="253" alt=""
                     class="img-responsive"/>
            </a>
        </div>
    </div>
    <div class="enable-menu">
        <ul>
            <li>
                <a href="${WEBROOT}/espace-assure#customer_board">
                    <spring:message code="customerpanel.inksNames.mainBoard"/>
                </a>
            </li>
            <li>
                <a href="${WEBROOT}/espace-assure#contracts_list">
                    <spring:message code="customerpanel.inksNames.contractsList"/>
                </a>
            </li>
            <li>
                <a href="${WEBROOT}/espace-assure#contrat_form">
                    <spring:message code="customerpanel.inksNames.contractForm"/>
                </a>
            </li>
        </ul>
    </div>
    <div ui-view></div>
    <!-- Modal -->
    <div ng-controller="SepaDocumentCtrl" class="modal fade" id="sepa_modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">
                        <spring:message code="customerpanel.sepaUploadModal.title"/>
                    </h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>
                        Veuillez retourner
                        <a ng-click="get_sepa()">
                            le document SEPA
                        </a>
                        une fois rempli :
                    </p>
                    <form novalidate>
                        <p>
                            <input ng-model="sepa" id="sepa" type="file"/>
                        </p>
                        <p>
                            <input ng-click="post_sepa()" type="submit" name="submit_sepa" value="Valider"/>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    const API_ACCESS_KEY   = "${API_ACCESS_KEY}";
    const PUBLIC_API_URI   = "/public";
    const CUSTOMER_API_URI = "/customer";
</script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js" onerror="src_on_error(this, '${WEBROOT}/resources/script/lib/angular.1.5.7.min.js')" defer></script>
<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular-route.min.js" onerror="src_on_error(this, '${WEBROOT}/public/script/angular-route.1.5.7.min.js')" defer></script>-->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.4.2/angular-ui-router.min.js" onerror="src_on_error(this, '${WEBROOT}/resources/script/lib/angular-ui-router.0.4.2.min.js')" defer></script>
<script type="text/javascript" src="https://raw.githubusercontent.com/eligrey/FileSaver.js/master/FileSaver.min.js" onerror="src_on_error(this, '${WEBROOT}/resources/script/lib/FileSaver.min.js')" defer></script>
<script type="text/javascript" src="${WEBROOT}/resources/script/app/customer-panel.js" defer></script>
<%@ include file="footer.jsp" %>