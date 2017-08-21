/**
 * Created by alexandremasanes on 24/10/2016.
 */
let insurances_select = $(" #insurances ");
$.datepicker.setDefaults( $.datepicker.regional[ "fr" ] );
$( "#purchase_date" ).datepicker({
        altField: "#datepicker",
        changeYear: true,
        changeMonth: true,
        closeText: 'Fermer',
        prevText: 'Précédent',
        nextText: 'Suivant',
        currentText: 'Aujourd\'hui',
        monthNames: ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'],
        monthNamesShort: ['Janv.', 'Févr.', 'Mars', 'Avril', 'Mai', 'Juin', 'Juil.', 'Août', 'Sept.', 'Oct.', 'Nov.', 'Déc.'],
        dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
        dayNamesShort: ['Dim.', 'Lun.', 'Mar.', 'Mer.', 'Jeu.', 'Ven.', 'Sam.'],
        dayNamesMin: ['D', 'L', 'M', 'M', 'J', 'V', 'S'],
        weekHeader: 'Sem.',
        dateFormat: 'dd-mm-yy'
});
$().ready(function() {
    let make_id = null;
    let model_id = null;
    let insurances = null;
    let clicked = false;

    $(" #insurances ").click(function() {
        if(!clicked) {
            console.log("kek");
            retrieve_insurances();
            clicked = true;
        }
    });

    $("#make").keyup(function() {
        let make_id_select = $("#make_id") ;
        make_id = make_id_select.val();
        if(make_id)
            return;
        let make_select = $("#make");
        if(!make_select.val())
            return;
        $.ajax({
            url     : WEBROOT+'/api/makes/'+make_select.val().trim()+'/',
            type    : 'GET',
            dataType: 'json',
            sucess: function(result, status, xhr) {
                let makes;
                if(!result)
                    return;
                makes = result;

                $.each(makes, function(key, make) {
                    make.label=(make.name);
                    make.value=(make.id);
                    delete make.name;
                    delete make.id;
                });
                //TODO externalize from ajax !!
                $("#make").autocomplete({
                    source: makes,
                    select: function (event, ui) {
                        $("#make").val(ui.item.label); // display the selected text
                        $("#make_id").val(ui.item.value); // save selected id to hidden input
                        make_id = ui.item.value;
                        return false;
                    },
                    focus: function(event, ui) {
                        return false;
                    }
                });

            }
        });
    });
    $("#model_year").keyup(function() { console.log(">>> ajax : models >>>");
        model_id=$("#model_id").val();
        let model_year_select = $("#model_year");
        if(model_id)
            return;
        //if(!model_year_select.val())
          //  return;
        $.ajax({
            url: WEBROOT+'/api/models/'+model_year_select.val().trim()+((make_id)?'/'+make_id:''),
            type: 'GET',
            dataType: 'json',
            complete: function(result, status) {
                let models_years;
                if(!result)
                    return;
                models_years = JSON.parse(result.responseText);
                console.log(models_years);
                if(!models_years.length)
                    return;
                $.each( models_years, function(key, model) { 
                    if(!make_id) {
                        model.make.label = model.make.name;
                        model.make.value = model.make.id;
                        delete model.make.name;
                        delete model.make.id;
                    }
                    model.label = (model.name + ' ' + model.year);
                    model.value = (model.id);
                    delete model.name;
                    delete model.year;

                });
                $("#model_year").autocomplete({
                    source: models_years,
                    select: function (event, ui) {
                        $("#model_year").val(ui.item.label); // display the selected text
                        $("#model_id").val(ui.item.value); // save selected id to hidden input
                        if(!make_id){
                            $("#make").val(ui.item.make.label);
                            $("#make_id").val(ui.item.make.value);
                        }

                        return false;
                    },
                    focus: function(event, ui){
                        return false;
                    }
                });
            }
        });
    });
});

function retrieve_insurances() {
    console.log(">>>retrieve_insurances>>>");

    $.ajax({
        url: WEBROOT + '/api/insurances',
        type: 'GET',
        dataType: 'json',
        success: function (result, status, xhr) {
            if (!result)
                return;
            console.log(result);
            insurances = result;
            bind_events();

        }
    });
}

function bind_events() {
    $.each(insurances, function (i, insurance) {
        let o = new Option(insurance.name, insurance.id);
        $(o).html(insurance.title);
        insurances_select.append(o);
    });
    $(" #insurances ").change(function(){
        let select = $(" #police ");
        select.val("");
        select.val(insurances[insurances_select.val()-1].default_amount);
    });
}