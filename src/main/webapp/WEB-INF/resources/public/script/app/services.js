let insurances_select = $(" #insurances ");
let amount_select = $(" #amount ");
let deductible_select = $(" #deductible ");
let default_amount_select = $(" #default_amount ");
let api_access_key = API_ACCESS_KEY;
let insurances = null;
let deductible = null;
let selected_insurance = null;


$(function() {
    console.log(">>>run>>>");
    let clicked = false;
    on_click(insurances_select, function() {
        console.log("clicked !");
        if(!clicked) {
            retrieve_insurances();
            clicked = true;
        }
    });

    console.log("<<<run<<<")
});

function retrieve_deductible(insurance, amount) {
    let retried;
    let config;

    $.ajax(config = {
        url        : PUBLIC_API_URI + '/insurances/' + insurance.id + '/deductibles',
        type       :'GET',
        dataType   :'json',
        data       : {
            amount : amount
        },
        beforeSend : function(request) {
            request.setRequestHeader('Authorization', API_ACCESS_KEY);
        },
        success    : function(result, status, xhr) {
            deductible = result;
        },
        error      : function(xhr, status, error) {
            console.log(status.error);
            if(!retried && status == 401) {
                retried = true;
                renew_token();
                $.ajax(config);
            }
        }
    });
}

function retrieve_insurances() {
    console.log(">>>retrieve_insurances>>>");
    let retried;
    let config;

    retried = false;
    console.log("ajax");
    $.ajax(config = {
        url         : PUBLIC_API_URI + '/insurances',
        type        : 'GET',
        dataType    : 'json',
        beforeSend : function(request) {
            request.setRequestHeader('Authorization', api_access_key);
        },
        success    : function(result, status, xhr) {
            console.log(result);
            insurances = result;
            bind_events();
        },
        error      : function(xhr, status, error) {
            console.log(status.error);
            if(!retried && status == 401) {
                retried = true;
                renew_token();
                $.ajax(config);
            }
        }
    });

    console.log("<<<retrieve_insurances<<<", insurances);
}

function bind_events() {
    $.each(insurances, function (i, insurance) {
        let o = new Option(insurance.name, insurance.id);
        $(o).html(insurance.title);
        insurances_select.append(o);
    });
    insurances_select.change(function() {
        amount_select.val(null);
        deductible_select.val(null);
        default_amount_select.val(null);
        let index = insurances_select.val()-1;
        if(index.toString().trim()) {
            selected_insurance = insurances[index];
            console.log(selected_insurance.id);
            default_amount_select.val(selected_insurance.default_amount);
        }
    });
    amount_select.keyup(function() {
        let amount = amount_select.val();
        console.log(selected_insurance, amount);
        if(selected_insurance && amount) {
            retrieve_deductible(selected_insurance, amount);
            deductible_select.val(parseFloat(deductible));
        }
        return false;
    });
}

function renew_token() {
    $.ajax({
        url        : PUBLIC_API_URI,
        type       : 'HEAD',
        beforeSend : function(request) {
            request.setRequestHeader('Authorization', api_access_key);
        },
        success    : function(result, status, xhr) {
            api_access_key = xhr.getResponseHeader('Authorization');
        },
        error      : function(xhr, status, error) {
            console.log(error);
        }
    });
}