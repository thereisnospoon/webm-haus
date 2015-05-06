<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<title>${title}</title>
</head>
<body>

	<div id="webm-view-container" class="modal fade" style="display: none">
		<div id="webm-view" class="modal-dialog">
			<div class="modal-content">
				<video autoplay controls class="modal-body" src="">
					Your browser doesn't support video tag
				</video>
			</div>
		</div>
	</div>

	<%@include file="header.jspf"%>
	<%@include file="webms_table.jspf"%>

</body>
</html>
