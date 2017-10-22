<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="customer_board" class="panel panel-default col-md-9">
    <div class="panel-heading">
        <label class="title" style="">
            <spring:message code="customerpanel.contract.panelTitle"/>{{contract.id}}
        </label>
    </div>
    <div class="panel-body" style="">
        <div class="post">
            <div class="entry">
                <div>
                    <table class="table table-striped">
                        <tbody>
                            <tr>
                                <th scope="row">#msg("id")</th>
                                <td>{{contract.id}}</td>
                            </tr>
                            <tr>
                                <th scope="row">#msg("insurance")</th>
                                <td>{{contract.insurance.name}}</td>
                            </tr>
                            <tr>
                                <th scope="row">#msg("annualAmount")</th>
                                <td>{{contract.amount}}</td>
                            </tr>
                            <tr>
                                <th scope="row">#msg("vehiclesRegistrationNumber")</th>
                                <td>{{contract.vehicle.registration_number}}</td>
                            </tr>
                            <tr>
                                <th scope="row">#msg("active")</th>
                                <td>{{contract.active ? "oui" : "non"}}</td>
                            </tr>
                        </tbody>
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
                <spring:message code="customerpanel.actionsPanel.title"/>
            </label>
        </div>
        <div ng-controller="NavCtrl" class="panel-body list-group">
            <ul class="actions-list">
                <li>
                    <button class="btn btn-md" id="show_sinisters_butt" ng-click="go('sinisters_list', {contract_key: contract_key})">
                        Voir les sinistres
                    </button>
                </li>
                <li>
                    <button class="btn btn-md" id="show_contract_butt" ng-click="go('contracts_list')">
                        Retour
                    </button>
                </li>
            </ul>
        </div>
    </div>
    <div id="todo" class="panel panel-default col-md-3">
        <div class="panel-heading">
            <label class="title">
                <spring:message code="customerpanel.todoPanel.title"/>
            </label>
        </div>
        <div ng-controller="NavCtrl" class="panel-body list-group">
            <ul class="actions-list">
                <li>
                    <button ng-if="!user_granted" type="button" class="btn btn-md" data-toggle="modal" data-target="#sepa_modal">
                        <spring:message code="customerpanel.todoPanel.sepaUpload.buttonTitle"/>
                    </button>
                </li>
            </ul>
        </div>
    </div>
</div>