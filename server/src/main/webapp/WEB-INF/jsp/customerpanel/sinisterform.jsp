<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div  id="contrat_form" class="panel panel-default col-md-9">
    <div class="panel-heading">
        <label class="title" style="width: 300px;">
            <spring:message code="customerpanel.sinisterForm.panelTitle"/>
        </label>
    </div>
    <div class="panel-body">
        <input type="radio" ng-model="type" value="1">Accident
        <input type="radio" ng-model="type" value="2">Sinistre sans tiers
        <form ng-if="type" id="sinister_form" novalidate>
            <div class="form-group">
                <label>
                    <spring:message code="date"/>
                </label>
                <input class="form-control" type="datetime" ng-model="sinister_form.date"  datepicker>
                <div class="form-group">
                    <label>
                        <spring:message code="time"/>
                    </label>
                    <input class="form-control" type="time" ng-model="sinister_form.time">
                </div>
            </div>
            <div ng-if="type == 2" class="form-group">
                <label>
                    <spring:message code="type"/>
                </label>
                <select class="form-control" size="1" ng-model="sinister_form.type_id" ng-click="load_types()">
                    <option ng-repeat="type in types" value="{{type.id}}" ng-click="select_type(type)">{{type.name}}
                </select>
            </div>
            <div ng-if="type == 1" class="form-group">
                <label>
                    <spring:message code="vehiclesRegistrationNumber"/>
                </label>
                <input type="text" ng-model="sinister_form.registration_number">
            </div>
            <div class="form-group">
                <label>
                    <spring:message code="comment"/>
                </label>
                <textarea class="form-control" ng-model="sinister_form.comment"></textarea>
            </div>
            <div class="form-group">
                <p>
                    <label>
                        <spring:message code="report"/>
                    </label>
                    <input ng-model="report_document" id="report_document" type="file"/>
                </p>
            </div>
            <button type="submit" ng-click="submit()">Valider</button>
        </form>
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
            <ul style="height: 246px; overflow: auto">
                <li>
                    <button class="btn btn-md"
                            id="show_contract_butt"
                            ng-click="go('sinisters_list', {contract_key:contract_key})">
                        Retour
                    </button>
                </li>
            </ul>
        </div>
    </div>
</div>