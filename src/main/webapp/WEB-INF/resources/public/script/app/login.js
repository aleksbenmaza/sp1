let auth_form = $("#auth_form");

on_click($("#log_button, #reg_button"), switch_action);

function switch_action(event) {
  
    event.preventDefault();
    if(this.id == "reg_button") {
            if(!($("#email").val() || $("#pwd").val())) {

                window.location.href = WEBROOT + "/inscription";
                return false;
            }
            auth_form.attr("action", WEBROOT + "/inscription");
    } else
        auth_form.attr("action", "/connexion");

    auth_form.submit();
    return false;
}
