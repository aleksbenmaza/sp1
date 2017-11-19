<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="customer_board" class="panel panel-default col-md-9">
    <div class="panel-heading">
        <label class="title">
            <spring:message code="customerpanel.index.panelTitle"/>
        </label>
    </div>
    <div class="panel-body" style="">
        <div class="post">
            <div class="entry">
                <div>
                    <table class="table table-striped">
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="lastName"/></th>
                            <td>{{customer.last_name}}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="firstName"/></th>
                            <td>{{customer.first_name}}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="address"/></th>
                            <td>{{customer.address}}, {{customer.zip_code}}, {{customer.city}}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="phone"/></th>
                            <td>{{customer.phone_number}}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="bonusMalus"/></th>
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
    <div class="panel panel-default col-md-3">
        <div class="panel-heading">
            <label class="title">
                <spring:message code="customerpanel.actionsPanel.title"/>
            </label>
        </div>
        <div ng-controller="NavCtrl" class="panel-body list-group">
            <ul class="actions-list">
                <li><button class="btn btn-md" id="show_contracts_butt" ng-click="go('contracts_list')">Voir les contrats</button></li>
            </ul>
        </div>
    </div>
    <div class="panel panel-default col-md-3">
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