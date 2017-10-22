<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="customer_board" class="panel panel-default col-md-9">
    <div class="panel-heading">
        <label class="title">
            <spring:message code="customerpanel.contractslist.panelTitle"/>
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
                            <td class="col-md-3"><spring:message code="insurance"/>)</td>
                            <td class="col-md-3"><spring:message code="annualAmount"/></td>
                            <td class="col-md-3"><spring:message code="vehiclesRegistrationNumber"/></td>
                            <td class="col-md-3"><spring:message code="active"/></td>
                        </tr>
                        <tr ng-repeat="contract in contracts">
                            <td class="col-md-1"><button ng-controller="NavCtrl" class="btn btn-md" ng-click="go('contract', {contract_key:$index+1})"></button></td>
                            <td class="col-md-3">{{contract.id}}</td>
                            <td class="col-md-3">{{contract.insurance.name}}</td>
                            <td class="col-md-3">{{contract.amount}}</td>
                            <td class="col-md-3">{{contract.vehicle.registration_number}}</td>
                            <td class="col-md-3">{{contract.active ? "oui" : "non"}}</td>
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
                    <button ng-if="user_granted" class="btn btn-md" id="new_contract_butt" ng-click="go('new_contract')">Nouveau contrat</button>
                    <button ng-if="!user_granted" class="btn btn-md btn-danger" data-toggle="modal" data-target="#sepa_modal">Nouveau contrat</button>
                </li>
                <li>
                    <button class="btn btn-md" id="show_contract_butt" ng-click="go('index')">Retour</button>
                </li>
            </ul>
        </div>
    </div>
</div>