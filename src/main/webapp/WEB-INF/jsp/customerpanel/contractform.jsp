<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div  id="contrat_form" class="panel panel-default col-md-9">
    <div class="panel-heading">
        <label class="title" style="width: 300px;">
            <spring:message code="customerpanel.contractForm.panelTitle"/>
        </label>
    </div>
    <div class="panel-body">
        <form novalidate id="contract_form">
            <div class="form-group">
                <label>
                    <spring:message code="customerpanel.contractForm.fields.insurance"/>
                </label>
                <select class="form-control" id="insurance" name="insurance" size="1" ng-model="contract_form.insurance_id" ng-click="load_insurances()">
                    <option ng-repeat="insurance in insurances" value="{{insurance.id}}" ng-click="select_insurance(insurance)">{{insurance.name}}
                </select>
                <div class="form-group">
                    <label>
                        <spring:message code="customerpanel.contractForm.fields.defaultAmount"/>
                    </label>
                    <div class="input-group">
                        <input class="form-control" id="police" value="{{selected_insurance.default_amount}}" readonly>
                        <span class="input-group-addon"><spring:message code="symbols.eur"/></span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label>
                    <spring:message code="customerpanel.contractForm.fields.vehicleMake"/>
                </label>
                <input class="ui-widget form-control" id="make" ng-model="str_0" ng-init="str_0=''" ng-keyup="search_makes(str_0)">
            </div>
            <div class="form-group">
                <label>
                    <spring:message code="customerpanel.contractForm.fields.vehicleModelAndYear"/>
                </label>
                <input class="ui-widget form-control" id="model_year" ng-model="str_1" ng-init="str_1=''" ng-keyup="search_models(str_1)">
                <input id="model_id" name="model_id" ng-model="contract_form.model_id" value="{{selected_model.id}}" readonly hidden/><br/>
            </div>
            <div class="form-group">
                <label>
                    <spring:message code="customerpanel.contractForm.fields.purchaseDate"/>
                </label>
                <br/>
                <input class="form-control" id="purchase_date" name="purchase_date" type="datetime" ng-model="contract_form.purchase_date" datepicker/>
            </div>
            <div class="form-group">
                <label>
                    <spring:message code="customerpanel.contractForm.fields.vinNumber"/>
                </label>
                <input class="form-control" name="vin_number" id="vin_number" ng-model="contract_form.vin_number"/>
            </div>
            <div class="form-group">
                <label>
                    <spring:message code="customerpanel.contractForm.fields.registrationNumber"/>
                </label>
                <input class="form-control" name="registration_number" ng-model="contract_form.registration_number" id="registration_number"/>
            </div>
            <div class="form-group">
                <p>
                    <label>
                        <spring:message code="customerpanel.contractForm.fields.registrationDocument"/>
                    </label>
                    <input ng-model="registration_document" id="registration_document" type="file"/>
                </p>
            </div>
            <button type="submit" id="submit_contract" ng-click="submit()">Valider</button>
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
                <li><button class="btn btn-md" id="show_contract_butt" ng-click="go('contracts_list')">Retour</button></li>
            </ul>
        </div>
    </div>
</div>