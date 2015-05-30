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

				var myXhr = $.ajaxSettings.xhr();
				myXhr.upload.onprogress = function(e) {

					if (e.lengthComputable) {
						progressBar.css('width', (100 * e.loaded / e.total) + '%');
					} else {
						console.log('Event is length uncomputable');
					}
				};
				return myXhr;
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

	var newWebMsQueryURL = '';
	var isUpdating = false;
	var isAllShowed = false;
	$(window).scroll(function() {

		if (isUpdating || isAllShowed) {
			return;
		}

		if( $(document).height() - $(window).scrollTop() - $(window).height() < 50) {

			isUpdating = true;
			loadMoreWebMs('/webm/list')
			isUpdating = false;
		}
	});

	function loadMoreWebMs(baseQueryPath) {

		var webmRows = $('tr', '#videos-container');
		var webmsPerRow = $('td', webmRows.first()).size();

		if (webmsPerRow > $('.webm-preview', webmRows.last()).size()) {

			console.log('All videos are loaded');
			isAllShowed = true;
			return;
		}

		var rowsToLoad = 5;
		var pageLoaded = webmRows.size() / rowsToLoad;

		var queryUrl = baseQueryPath + '?page=' + pageLoaded +
			'&size=' + rowsToLoad*webmsPerRow + '&sort=postedWhen,desc'

		console.log('Query url: ' + queryUrl);

		$.getJSON(queryUrl, function(data) {

			console.log('Received data:');
			console.log(data);

			appendWebMs(data);
		});

		function appendWebMs(webms) {

			var table = $('table', '#videos-container');
			var tdElement = $('td', webmRows.first()).first();
			var newRows = [];
			var newRow = null;
			for (var i = 0; i < webms.length; i++) {

				if (i % webmsPerRow == 0) {
					newRow = $('<tr/>');
					newRows.push(newRow);
				}

				var webm = webms[i];
				var newTdElement = tdElement.clone();
				$('.webm-name', newTdElement).text(webm["name"]);
				$('a', newTdElement).attr('href', '/webm/data/' + webm.fileId);
				$('img', newTdElement).attr('src', '/webm/preview/' + webm.previewId);
				$('.views', newTdElement).text(webm.viewsCounter);
				$('.likes', newTdElement).text(webm.likesCounter);

				newRow.append(newTdElement)
			}

			for (var i = 0; i < newRows.length; i++) {
				table.append(newRows[i]);
			}
		}
	}

	$('#webm-view-container').on('hide.bs.modal', function() {

		$('video', $(this))[0].pause();
	});

	$('#videos-container').delegate('.webm-preview a', 'click', function(e) {

		e.preventDefault();
		$('video', '#webm-view').attr('src', $(this).attr('href'));
		$('#webm-view-container').modal('show');
	});

	$('#sign-in-button').click(function() {
		$('#login-form-container').modal('show');
	});

	$('#login-form').ajaxForm(function() {
		alert('Form submitted')
	});
});