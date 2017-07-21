<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 04/04/2017
  Time: 19:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div  id="contrat_form" class="panel panel-default col-md-9">
    <div class="panel-heading" style=" background: #353535; color: #AFAFAF;">
        <label class="title" style="width: 300px;">
            <spring:message code="customerpanel.contractFormLabel"/>
        </label>
    </div>
    <div class="panel-body">
        <form novalidate id="contract_form" method="post">
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
                        <span class="input-group-addon">€</span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label>
                    Marque du véhicule :
                </label>
                <input class="ui-widget form-control" id="make" ng-model="str_0" ng-init="str_0=''" ng-keyup="search_makes(str_0)">
            </div>
            <div class="form-group">
                <label>
                    Modèle et année du véhicule :
                </label>
                <input class="ui-widget form-control" id="model_year" ng-model="str_1" ng-init="str_1=''" ng-keyup="search_models(str_1)">
                <input id="model_id" name="model_id" ng-model="contract_form.model_id" value="{{selected_model.id}}" readonly hidden/><br/>
            </div>
            <div class="form-group">
                <label>
                    Date d'achat :
                </label>
                <br/>
                <input class="form-control" id="purchase_date" name="purchase_date" type="datetime" ng-model="contract_form.purchase_date" datepicker/>
            </div>
            <div class="form-group">
                <label>
                    Numéro de série :
                </label>
                <input class="form-control" name="vin_number" id="vin_number" ng-model="contract_form.vin_number"/>
            </div>
            <div class="form-group">
                <label>
                    Immatriculation du véhicule :
                </label>
                <input class="form-control" name="registration_number" ng-model="contract_form.registration_number" id="registration_number"/>
            </div>
            <div class="form-group">
                <p>
                    <label>
                        Carte grise du véhicule :
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
        <div class="panel-heading" style=" background: #353535; color: #AFAFAF;">
            <label class="title" style="">
                <spring:message code="customerpanel.contractsListLabel"/>
            </label>
        </div>
        <div ng-controller="NavCtrl" class="panel-body list-group">
            <ul style="height: 246px; overflow: auto">
                <li><button class="btn btn-md" id="show_contract_butt" ng-click="go('espace-assure/vos-contrats')">Retour</button></li>
            </ul>
        </div>
    </div>
</div>
<!--<script type="text/javascript" src="${WEBROOT}/public/script/contract-form.js" defer></script> -->