<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="customer_board" class="panel panel-default col-md-9">
    <div class="panel-heading">
        <label class="title">
            <spring:message code="customerpanel.sinisterslist.panelTitle"/> {{sinisters[0].contract.id}}
        </label>
    </div>
    <div class="panel-body" style="height: 246px; overflow: auto">
        <div class="post">
            <div class="entry">
                <div>
                    <table class="table-striped">
                        <tr>
                            <td class="col-md-1"></td>
                            <td class="col-md-3"><spring:message code="id"/></td>
                            <td class="col-md-3"><spring:message code="date"/></td>
                            <td class="col-md-3"><spring:message code="time"/></td>
                            <td class="col-md-3"><spring:message code="closed"/></td>
                            <td class="col-md-3"><spring:message code="responsibilityRate"/></td>
                            <td class="col-md-3"><spring:message code="type"/></td>
                        </tr>
                        <tr ng-repeat="sinister in sinisters">
                            <td class="col-md-1"><button ng-controller="NavCtrl" class="btn btn-md" ng-click="go('contract', {contract_key:$index+1})"></button></td>
                            <td class="col-md-3">{{sinister.id}}</td>
                            <td class="col-md-3">{{sinister.date}}</td>
                            <td class="col-md-3">{{sinister.time}}</td>
                            <td class="col-md-3">{{sinister.closed ? "oui" : "non"}}</td>
                            <td class="col-md-3">{{sinister.responsibility_rate || sinister.responsibility_rate == 0 ? sinister.responsibility_rate : ""}}</td>
                            <td class="col-md-3">{{sinister.type_name ? sinister.type_name : ""}}</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div>
    <div id="contracts_list" class="panel panel-default col-md-3">
        <div class="panel-heading">
            <label class="title">
                <spring:message code="customerpanel.navPanelTitle"/>
            </label>
        </div>
        <div ng-controller="NavCtrl" class="panel-body list-group">
            <ul style="height: 246px; overflow: auto">
                <li>
                    <button ng-if="user_granted" class="btn btn-md" id="new_sinister_butt" ng-click="go('new_sinister', {contract_key:contract_key})">Nouveau sinistre</button>
                    <button ng-if="!user_granted" class="btn btn-md btn-danger" data-toggle="modal" data-target="#sepa_modal">Nouveau sinistre</button>
                </li>
                <li>
                    <button class="btn btn-md" id="show_contracts_list_butt" ng-click="go('contracts_list')">Retour</button>
                </li>
            </ul>
        </div>
    </div>
</div>