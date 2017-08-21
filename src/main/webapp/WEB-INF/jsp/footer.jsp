<%--
  Created by IntelliJ IDEA.
  User: alexandremasanes
  Date: 20/02/2017
  Time: 11:42
  To change this template use File | Settings | File Templates.
--%>
        <div id="footer">
            <p>
                &copy;
                <a href="${WEBROOT}">
                    Assurance Automobile Aixoise
                </a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                by
                <a href="">
                    SysLog
                </a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
            </p>
        </div>
        <c:if test="${not empty messageCode}">
            <div class="alert alert-success">
                <strong><spring:message code="${messageCode}"/></strong>
            </div>
        </c:if>
        <script type="text/javascript">
            const WEBROOT = "${WEBROOT}";
        </script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js" onerror="src_on_error(this, '${WEBROOT}/resources/script/lib/jquery.3.1.1.min.js')"></script>
        <!-- Latest compiled and minified JavaScript -->
        <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous" onerror="src_on_error(this, '${WEBROOT}/resources/script/lib/bootstrap.3.3.7.min.js')"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" onerror="src_on_error(this, '${WEBROOT}/resources/script/lib/jquery-ui.1.12.1.min.js')"></script>
        <script type="text/javascript" src="${WEBROOT}/resources/script/app/common.js"></script>
    </body>
</html>