$(function() {

	var MAX_FILE_SIZE = 1024*1024*20;

	var dropArea = $('#droparea');

	dropArea.on("dragstart dragenter dragover dragleave drag drop dragend", function(event) {

		event.stopPropagation();
		event.preventDefault();
	});

	dropArea.bind('dragover', function() {
		$(this).css('background-color', 'rgba(151,165,171, 0.2)');
	});

	dropArea.bind('dragleave', function() {
		$(this).css('background-color', 'initial');
	});

	dropArea.bind('drop', function(event) {

		dropArea.css('background-color', 'initial');

		var droppedFile = event.originalEvent.dataTransfer.files[0];
		console.log(droppedFile);

		var errorDialog = $('#errorDialog');
		var textContent = $('.modal-body p', errorDialog);
		if (droppedFile.size > MAX_FILE_SIZE) {

			textContent.text('File size should be less then 20 MB');
			errorDialog.modal('show');
			return
		}
		if (droppedFile.type != 'video/webm') {

			textContent.text('File format is not supported');
			errorDialog.modal('show');
			return;
		}

		var formData = new FormData($('#file-input')[0]);
		formData.append('file', droppedFile);

		$('.progress').css('display', 'block');
		var progressBar = $('.progress-bar');

		dropArea.hide('drop', {direction: 'up'}, 'slow');
		$('.panel').show('fold', 1000);

		$.ajax({
			type: 'POST',
			contentType: false,
			processData: false,
			url: '/upload',
			data: formData,
			xhr: function() {

				var xhr = new window.XMLHttpRequest();
				xhr.addEventListener('progress', function(event) {
					if (event.lengthComputable) {
						progressBar.css('width', (100 * event.loaded / event.total) + '%');
					}
				});
				return xhr;
			},
			success: function (data) {

				console.log(data);

				progressBar.removeClass('progress-bar-info');
				progressBar.addClass('progress-bar-success');

				$('.progress-bar').attr('id', data.webMId);
			}
		});
	});

	$('#save-btn').click(function (){

		var metaData = {
			name: $('#webMName').val(),
			description: $('#webMDescription').val(),
			fileId: $('.progress-bar').attr('id'),
			tagsString: $('#webMTags').val(),
			date: new Date,
			timeZoneOffset: new Date().getTimezoneOffset()
		};
		$.post('/upload/meta', metaData, function(data) {

			console.log(data);
		});
	});
});