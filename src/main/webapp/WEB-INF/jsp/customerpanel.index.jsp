<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 25/03/2017
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="customer_board" class="panel panel-default col-md-9">
    <div class="panel-heading" style="background: #353535; color: #AFAFAF;">
        Panneau de board
    </div>
    <div ng-model="customer"  class="panel-body" style="">
        <div class="post">
            <div class="entry">
                <div>
                    <h2>
                        Espace Personnel
                    </h2>
                    <table class="table table-striped">
                        <tbody>
                        <tr>
                            <th scope="row">Nom</th>
                            <td>{{customer.last_name}}</td>
                        </tr>
                        <tr>
                            <th scope="row">Prenom</th>
                            <td>{{customer.first_name}}</td>
                        </tr>
                        <tr>
                            <th scope="row">Adresse</th>
                            <td>{{customer.address}}, {{customer.zip_code}}, {{customer.city}}</td>
                        </tr>
                        <tr>
                            <th scope="row">Téléphone</th>
                            <td>{{customer.phone_number}}</td>
                        </tr>
                        <tr>
                            <th scope="row">Bonus malus</th>
                            <td>{{customer.bonus_malus}}</td>
                        </tr>
                        </tbody>
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
                <li><button class="btn btn-md" id="show_contract_butt" ng-click="go('espace-assure/vos-contrats')">Voir les contrats</button></li>
            </ul>
        </div>
    </div>
</div>
