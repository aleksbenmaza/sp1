/**
 * Created by alexandremasanes on 05/01/2017.
 */

const API_ABSOLUTE_URI = "http://api." + location.hostname;

const log = console.log;

$.datepicker.setDefaults( $.datepicker.regional[ "fr" ] );

let datepicker_options = {
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
    dateFormat: 'dd/mm/yy'
};

$( "input[type=datetime]" ).datepicker(datepicker_options);

function on_click(selector, callback) {
    selector.on('click touchstart', callback);
}

function src_on_error(element, uri) {
    element.onerror = null;
    element.src     = uri;
    return true;
}