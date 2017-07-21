<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 03/04/2017
  Time: 12:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<p>Veuillez retourner <a href="${WEBROOT}/download-contrat-${key}">le contrat</a> signé :</p>
<form name="upload_contrat" enctype="multipart/form-data">
    <p>
        <input name="contrat" type="file">
    </p>
    <p>
        <button type="submit" id="upload_button" class="btn btn-default">Valider</button>
    </p>
</form>
