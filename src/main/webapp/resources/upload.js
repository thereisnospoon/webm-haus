$(function() {

	var MAX_FILE_SIZE = 1024*1024*20;

	var dropArea = $('#droparea');
	var webmPostAttrs = ['_id', 'name', 'description', 'previewId', 'fileId', 'postedWhen', 'duration', 'postedBy',
		'tags'];

	var currentWebm = {};

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

		var progress = $('.progress');
		progress.css('display', 'block');
		var progressBar = $('.progress-bar');

		dropArea.hide('drop', {direction: 'up'}, 'fast');

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

				setTimeout(function() {
					progressBar.removeClass('progress-bar-info');
					progressBar.addClass('progress-bar-success');
				}, 300);

				setTimeout(function() {
					progress.hide('drop', {direction: 'down'}, 'slow');
				}, 600);


				setTimeout(function() {

					if (!data.fileId) {

						console.log('Emtpy response is returned');

						return
					}

					for (var i in webmPostAttrs) {
						currentWebm[webmPostAttrs[i]] = data[webmPostAttrs[i]];
					}

					var webMPath = '/webm/data/' + data.fileId;
					var videoElement = $('<video controls></video>');
					var srcElement = videoElement.prepend($('<source src="' + webMPath + '" type="video/webm">'));
					videoElement.prepend(srcElement);
					$('.panel-body').prepend(videoElement);
				}, 2000);

				if (data.fileId) {
					$('.panel').show('fold', 1000);
					$('#control-buttons').css('display', 'block');
				}
			}
		});
	});

	$('#save-btn').click(function (){

		currentWebm['description'] = $('#webMDescription').val();
		currentWebm['tags'] = $('#webMTags').val().split(', ');
		currentWebm['name'] = $('#webMName').val();

		console.log('Sending meta-data:');
		console.log(currentWebm);

		$.ajax({
			type: 'POST',
			contentType: 'application/json',
			url: '/upload/meta',
			data: JSON.stringify(currentWebm),
			success: function(data) {

				console.log('Metadata posted successfully')
			}
		})
	});
});