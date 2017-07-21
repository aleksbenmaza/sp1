<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 07/04/2017
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="customer_board" class="panel panel-default col-md-9">
    <div class="panel-heading" style="background: #353535; color: #AFAFAF;">
        Panneau de board
    </div>
    <div ng-model="contracts"  class="panel-body" style="height: 246px; overflow: auto">
        <div class="post">
            <div class="entry">
                <div>
                    <h2>
                        Vos contrats
                    </h2>
                    <table class="table-striped">
                        <tr>
                            <td class="col-md-3">Id</td>
                            <td class="col-md-3">Type garantie</td>
                            <td class="col-md-3">Montant annuel</td>
                            <td class="col-md-3">Immatriculation du véhicule</td>
                            <td class="col-md-3">Actif</td>
                        </tr>

                        <tr ng-repeat="contract in contracts">
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
    <div  id="contracts_list" class="panel panel-default col-md-3">
        <div class="panel-heading" style=" background: #353535; color: #AFAFAF;">
            <label class="title" style="">
                <spring:message code="customerpanel.contractsListLabel"/>
            </label>
        </div>
        <div ng-controller="NavCtrl" class="panel-body list-group">
            <ul style="height: 246px; overflow: auto">
                <li><button class="btn btn-md" id="new_contract_butt" ng-click="go('espace-assure/nouveau-contrat')">Nouveau contrat</button></li>
                <li><button class="btn btn-md" id="show_contract_butt" ng-click="go('espace-assure/')">Retour</button></li>
            </ul>
        </div>
    </div>
</div>
