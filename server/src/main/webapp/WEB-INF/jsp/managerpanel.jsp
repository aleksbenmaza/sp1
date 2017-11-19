<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 04/11/2017
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="header.jsp" %>
<spring:eval expression="@host" var="host"/>
<div id="app" class="container"></div>
<script defer>
    const MANAGER_API_BASE_URI = "http://${host.managerApiSubdomain}.${host.domainName}";
    const PUBLIC_API_BASE_URI   = "http://${host.publicApiSubdomain}.${host.domainName}";
</script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/vue-resource@1.3.4" defer></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/vuex/3.0.1/vuex.min.js" defer></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/vue-router/3.0.1/vue-router.min.js" defer></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.2/vue.min.js" onerror="src_on_error(this, '${WEBROOT}/resources/public/script/lib/vue.2.5.2.min.js'" defer></script>
<script type="text/javascript" src="${WEBROOT}/resources/public/script/app/manager-panel.js" defer></script>
<%@ include file="footer.jsp" %>
