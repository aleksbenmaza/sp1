/**
 * Created by alexandremasanes on 06/04/2017.
 */
/* global angular */

'use strict';

const CUSTOMER_API_URI = API_ABSOLUTE_URI + "/customer";
const PUBLIC_API_URI   = API_ABSOLUTE_URI + "/public";

let api_access_key = API_ACCESS_KEY;

let app = angular.module('CustomerPanel', [
    'ui.router'
]);

app.directive("datepicker", function () {
    return {
        restrict: "A",
        require: "ngModel",
        link: function (scope, elem, attrs, ngModelCtrl) {
            let updateModel = function (dateText) {
                scope.$apply(function () {
                    ngModelCtrl.$setViewValue(dateText);
                });
            };
            let options = datepicker_options;
            options.onSelect = function (dateText) {
                updateModel(dateText);
            };
            elem.datepicker(options);
        }
    }
});

app.factory('httpResponseErrorInterceptor', ['$q', '$injector', function($q, $injector) {
    return {
        responseError : function(response) {

            if (!response.retrying && response.status === 401) {
                response.retrying = true;
                // should retry
                let deferred = $q.defer();
                let $http = $injector.get('$http');
                $.ajax({
                    url        : API_ABSOLUTE_URI,
                    type       : 'HEAD',
                    beforeSend : function(request) {
                        request.setRequestHeader('Authorization', api_access_key);
                    },
                    success    : function(result, status, xhr) {
                        console.log("error -> ok : should retry");
                        deferred.resolve(xhr.getResponseHeader('Authorization'));
                    },
                    error      : function(xhr, status, error) {
                        console.log("error -> bad : should give up");
                        deferred.resolve(null);
                    }
                });
                console.log(deferred.promise);
                if(deferred.promise.$$state.value == null)
                    return $q.reject(response);
                response.config.headers['Authorization'] = api_access_key = deferred.promise.$$state.value;
                return $http(response.config);
            }

            return $q.reject(response);
        }
    };
}]);

app.config(function($httpProvider) {
    $httpProvider.interceptors.push('httpResponseErrorInterceptor');
});

app.controller('NavCtrl', ['$scope', '$state', '$http', '$q', function($scope, $state, $http, $q) {
    let defer = $q.defer();
    $scope.user_is_granted = $http.get(CUSTOMER_API_URI + '/granted', {
        responseType : 'text'
    }).success(function(data) {
        console.log(data);
        defer.resolve(data);
    }).error(function(data, status) {
        console.log(status);
    });
    $scope.user_granted = defer.promise;

    $scope.go = function(state, params = {}) {
        console.log(state, params);
        $state.go(state, params);
    };

}]);


app.controller('IndexCtrl', ['$scope', '$http', function($scope, $http) {

    $http.get(CUSTOMER_API_URI, {
        responseType: 'json'
    }).success(function(customer) {
        console.log(customer);
        $scope.customer = customer;
    }).error(function(data, status) {
        console.log(status);
    });
}]);

app.controller('ContractsListCtrl', ['$scope', '$http', function($scope, $http) {

    $http.get(CUSTOMER_API_URI + '/contracts', {
        responseType: 'json'
    }).success(function(contracts) {
         $scope.contracts = contracts;
         console.log(contracts);
    }).error(function(data, status) {
         console.log(status);
    });
}]);

app.controller('ContractFormCtrl', ['$scope', '$http', function($scope, $http) {

    $scope.contract_form = {};

    $scope.submit = function() {
        let data;
        let exploded_date;
        data = $scope.contract_form;
        if(data.purchase_date && (exploded_date = data.purchase_date.split('/')).length == 3)
            data.purchase_date = exploded_date.reverse().join('-');
        parse_file_then_send($("#registration_document")[0].files[0], $http, CUSTOMER_API_URI + '/contracts', $scope.contract_form, 'registration_document');
    };

    $scope.load_insurances = function() {
        if($scope.insurances)
            return;
        $scope.insurances = $http.get(PUBLIC_API_URI + '/insurances', {
           responseType : 'json'
        }).success(function(insurances) {
            $scope.insurances = insurances;
            console.log(insurances);
        }).error(function(data, status){
            console.log(status);
        });
    };

    $scope.select_insurance = function(insurance) {
        $scope.selected_insurance = insurance;
    };

    $scope.search_makes = function(str) {
        str = str.trim();
        if(!str)
            return;
        $http.get(CUSTOMER_API_URI+'/makes/'+str, {
            responseType : 'json'
        }).success(function(makes) {
            let makes_source;

            console.log(makes);

            if(!makes)
                return;

            makes_source = [];

            $.each(makes, function(key, make) {
                makes_source.push({
                    label : make.name,
                    value : function() {
                        return key;
                    }
                });
            });
            //TODO externalize from ajax !!

            $("#make").autocomplete({
                source: makes_source,
                select: function (event, ui) {
                    event.preventDefault();
                    console.log(ui); console.log(makes[ui.item.value()]);

                    $scope.selected_make = makes[ui.item.value()];
                    //$scope.make_id = $scope.selected_make.id; // save selected id to hidden input
                    $("#make").val(ui.item.label); // display the selected text
                    if($scope.selected_model && $scope.selected_model.make != $scope.selected_make) {
                        $scope.selected_model = null;
                        $( "#model_year" ).val("");
                    }

                    return false;
                },
                focus: function(event, ui) {
                    return false;
                }
            });
        }).error(function(data, status) {
            console.log(status);
        });
    };

    $scope.search_models = function(str) {
        console.log(">>> search_models <<<");
        str = str.trim();
        if(!str)
            return;
        $http.get(CUSTOMER_API_URI + (($scope.selected_make) ? '/makes/' + $scope.selected_make.id : '') + '/models/' + str, {
            responseType : 'json'
        }).success(function (models) {
            let models_source;

            console.log(models);

            if(!models)
                return false;

            models_source = [];

            $.each(models, function(key, model) {
                models_source.push({
                    label : model.name + ' ' + model.year,
                    value : function() {
                       return key;
                    }
                });
            });

            $("#model_year").autocomplete({
                source: models_source,
                select: function (event, ui) {
                    $("#model_year").val(ui.item.label); // display the selected text
                    $scope.selected_model = models[ui.item.value()]; // save selected id to hidden input
                    $scope.contract_form.model_id = $scope.selected_model.id;
                    if(!$scope.selected_make) {
                        $scope.selected_make = $scope.selected_model.make;
                        $("#make").val($scope.selected_make.name);
                    }

                    return false;
                },
                focus: function(event, ui){
                    return false;
                }
            });
        }).error(function(data, status) {
            console.log(status);
        });
    };
}]);

app.controller('ContractFileUploadCtrl', ['$scope', '$http', function ($scope, $http) {

}]);

app.controller('ContractCtrl', ['$scope', '$http', '$stateParams', function ($scope, $http, $stateParams) {
    console.log($stateParams);
    $scope.contract_key = $stateParams.contract_key;
    $http.get(CUSTOMER_API_URI + '/contracts/' + $stateParams.contract_key, {
        responseType : 'json'
    }).success(function(contract) {
        $scope.contract = contract;
    }).error(function(data, status) {
        console.log(status, data);
    });
}]);

app.controller('SinistersListCtrl', ['$scope', '$http', '$stateParams', function ($scope, $http, $stateParams) {
    $scope.contract_key = $stateParams.contract_key;

    $http.get(CUSTOMER_API_URI + '/contracts/' + $stateParams.contract_key + '/sinisters', {
        responseType : 'json'
    }).success(function(sinisters) {
        $scope.sinisters = sinisters;
    }).error(function(data, status) {
        console.log(status, data);
    });
}]);

app.controller('SinisterFormCtrl', ['$scope', '$http', '$stateParams', function ($scope, $http, $stateParams) {

    $scope.contract_key = $stateParams.contract_key;

    $scope.sinister_form = {};

    $scope.load_types = function() {
      $http.get(CUSTOMER_API_URI + '/contract/' + $scope.contract_key + '/sinisters/types', {
          responseType : 'json'
      })  .success(function(types) {
          $scope.types = types;
      }).error(function(data, status) {
          console.log(status, data);
      });
    };

    $scope.submit = function() {
        let data;
        let exploded_date;
        data = $scope.sinister_form;
        if(data.date && (exploded_date = data.date.split('/')).length == 3)
            data.date = exploded_date.reverse().join('-');
        parse_file_then_send(
            $("#report_document")[0].files[0],
            $http,
            CUSTOMER_API_URI + '/contracts/' + $scope.contract_key + '/sinisters',
            $scope.sinister_form,
            'report_document'
        );
    };
}]);

app.controller('SepaDocumentCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.form = {};

    $scope.get_sepa = function() {
        $http.get(CUSTOMER_API_URI + '/sepa', {
            responseType : 'json'
        }).success(function(sepa) {
            save_as(new Blob(sepa, {type: "application/pdf"}), "sepa.pdf");
        }).error(function(data, status) {
            console.log(status);
        });
    }

    $scope.post_sepa = function() {
        parse_file_then_send($("#sepa")[0].files[0], $http, CUSTOMER_API_URI + '/sepa', null, 'sepa');
    }
}]);

app.config(['$locationProvider', '$stateProvider', '$urlRouterProvider', '$httpProvider', function($locationProvider, $stateProvider, $urlRouterProvider, $httpProvider) {
    $locationProvider.html5Mode(true);
    $locationProvider.hashPrefix('');
    $httpProvider.interceptors.push('httpResponseErrorInterceptor');
    //$urlRouterProvider.otherwise('/espace-assure');

    $stateProvider.state('index', {
        url         : WEBROOT + '/espace-assure',
        templateUrl : CUSTOMER_API_URI + '/template/index.html',
        controller  : 'IndexCtrl'
    });

    $stateProvider.state('new_contract', {
        url         : WEBROOT + '/espace-assure/nouveau-contrat',
        templateUrl : CUSTOMER_API_URI+'/template/contractform.html',
        controller  : 'ContractFormCtrl'
    });

    $stateProvider.state('contracts_list', {
        url         : WEBROOT + '/espace-assure/vos-contrats',
        templateUrl : CUSTOMER_API_URI+'/template/contractslist.html',
        controller  : 'ContractsListCtrl'
    });


    $stateProvider.state('contract', {
        url         : WEBROOT + '/espace-assure/contrat/:contract_key',
        templateUrl : CUSTOMER_API_URI+'/template/contract.html',
        controller  : 'ContractCtrl',
    });

    $stateProvider.state('sinisters_list', {
        url         : WEBROOT + '/espace-assure/contrat/:contract_key/sinistres',
        templateUrl : CUSTOMER_API_URI + '/template/sinisterslist.html',
        controller  : 'SinistersListCtrl'
    });

    $stateProvider.state('new_sinister', {
        url         : WEBROOT + '/espace-assure/contrat/:contract_key/nouveau-sinistre',
        templateUrl : CUSTOMER_API_URI + '/template/sinisterform.html',
        controller  : 'SinisterFormCtrl'
    });

    $httpProvider.defaults.headers.common['Authorization'] = function() {
        return api_access_key
    };
    console.log($httpProvider.defaults.headers.common);
    delete $httpProvider.defaults.headers.common['Cookie'];
}]);

function parse_file_then_send(target_element, http, uri, data, file_field) {
    let result;

    if(!target_element)
        return;

    let reader = new FileReader;
    reader.onload = function() {
        let array;
        let arrayBuffer = this.result;
        array = new Uint8Array(arrayBuffer);
        if(data)
            data[file_field] = Array.from(array);
        else
            data = Array.from(array);
        http.post(uri, data).success(function() {

        }).error(function() {

        });

    };
    reader.readAsArrayBuffer(target_element);
}

function save_as(blob, fileName) {
    let url = window.URL.createObjectURL(blob);

    let anchorElem      = document.createElement("a");
    anchorElem.style    = "display: none";
    anchorElem.href     = url;
    anchorElem.download = fileName;

    document.body.appendChild(anchorElem);
    anchorElem.click();

    document.body.removeChild(anchorElem);

    // On Edge, revokeObjectURL should be called only after
    // a.click() has completed, atleast on EdgeHTML 15.15048
    setTimeout(function() {
        window.URL.revokeObjectURL(url);
    }, 1000);
}