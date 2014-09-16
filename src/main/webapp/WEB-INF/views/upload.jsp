<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Upload WebM</title>
    <link rel="stylesheet" href="/resources/main.css">
    <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/themes/smoothness/jquery-ui.css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
</head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
<script src="/resources/upload.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<body>

<div id="droparea">
    <span>Drop file here</span>
</div>

<div class="modal fade" id="errorDialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
	                <span aria-hidden="true">&times;</span>
	                <span class="sr-only">Close</span></button>
                <h4 class="modal-title">Error</h4>
            </div>
            <div class="modal-body">
                <p>File format is not supported</p>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="panel panel-default" style="display: none">
	<div class="panel-heading">
		<h3 class="panel-title">WebM parameters</h3>
	</div>
	<div class="panel-body">
		<input id="webMName" type="text" class="form-control" placeholder="WebM name">
		<textarea id="webMDescription" class="form-control" placeholder="Description"></textarea>
		<input id="webMTags" type="text" class="form-control" placeholder="Tags">

		<div id="control-buttons">
			<button type="button" id="save-btn" data-loading-text="Saving" class="btn btn-primary">Save</button>
			<button type="button" id="cancel-btn" class="btn btn-default">Cancel</button>
		</div>
	</div>
</div>

<input type="hidden" id="file-input">

<div class="progress" style="display: none">
	<div class="progress-bar progress-bar-info progress-bar-striped" role="progressbar"
	     aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 0;">
	</div>
</div>

</body>
</html>