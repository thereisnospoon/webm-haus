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
						dropArea.show('blind', 'fast');

						var alrt = $('#video-present-alert');
						alrt.show();
						alrt.find('.close').click(function() {
							$(this).parent().hide();
						});

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

	var saveBtn = $('#save-btn');

	$('#webMName').keyup(function() {

		if ($(this).val()) {
			saveBtn.removeAttr('disabled');
		} else {
			saveBtn.attr('disabled', 'disabled');
		}
	});

	saveBtn.click(function (){

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
			success: function() {

				console.log('Metadata posted successfully');
				window.location.replace('/');
			}
		})
	});

	var isUpdating = false;
	var isAllShowed = false;
	$('#load-more').click(function() {

		if (isUpdating || isAllShowed) {
			return;
		}

		isUpdating = true;
		loadMoreWebMs();
		isUpdating = false;
	});

	function loadMoreWebMs() {

		var webmRows = $('tr', '#videos-container');
		var webmsPerRow = 4;

		var lastWebmId = webmRows.last().find('.webm-id').last().text();
		var queryUrl = '/webm/next/' + lastWebmId + '?webmQuantity=8';

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

				$('.webm-id', newTdElement).text(webm.id);
				$('.webm-author', newTdElement).text(webm.postedBy);
				$('.webm-tags', newTdElement).text(webm.tags);
				$('.webm-postedWhen').text(webm.prettyDate);
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

	$('#sign-in-button').click(function(event) {
		event.preventDefault();
		$('#login-form-container').modal('show');
	});

	var authFailedDiv = $('#auth-failed-div');
	$('#login-form').ajaxForm(function(response) {

		console.log('Response:');
		console.log(response);

		if (response.status && 'failed' == response.status) {
			authFailedDiv.show();
		} else {
			console.log('auth successful');
			authFailedDiv.hide();
			$('#login-form-container').modal('hide');
			$('.anon-controls').hide();

			var userControl = $('<a id="user-ref" class="auth-controls" href="">' +
				'<img src="/resources/default_avatar.png"/>' +
				response.username +
				'</a>');

			var loginControls = $('#login-controls');
			loginControls.append(userControl);
			loginControls.append('<a href="/security/j_spring_security_logout"><span class="glyphicon glyphicon-off"/></a>')
			
			var commentPostingEl = $('<div id="comment_and_like">' +
					'<span id="like-btn" class="glyphicon glyphicon-heart"></span>' + 
					'<input type="text" id="comment-input" placeholder="Post a comment" title="Press enter to submit"/>' +
					'</div>');
			
			$('#video-meta').append(commentPostingEl);
		}
	});

	$('#sign-up-button').click(function(event) {
		event.preventDefault();
		$('#sign-up-form-container').modal('show');
	});

	var signUpForm = $('#sign-up-form');
	signUpForm.find('.login-form-button').click(function() {

		signUpForm.find('.error_message').text('');
		signUpForm.find('.error_message').hide();

		var pass = signUpForm.find('.login-password').find('input').val();
		var repeatedPass = signUpForm.find('.login-password-repeat').find('input').val();

		if (!pass || !repeatedPass || repeatedPass != pass) {

			var errorEl = signUpForm.find('.login-password').find('.error_message');
			errorEl.text('Passwords should correspond');
			errorEl.show();
			return;
		}

		var newUser = {password: pass, username: signUpForm.find('.login-username').find('input').val()};
		$.ajax({
			type: 'POST',
			url: '/security/sign-up',
			contentType: 'application/json',
			data: JSON.stringify(newUser),
			success: function(data) {

				console.log(data);

				if (data.status && data.status == 'failed') {

					for (var j = 0; j < data.errors.length; j++) {

						var errorForField = '.login-' + data.errors[j].field;
						var errorMessageElement = signUpForm.find(errorForField).find('.error_message');
						errorMessageElement.text(data.errors[j].defaultMessage);
						errorMessageElement.show();
					}
				} else {

					signUpForm.find('input').val('');
					$('#sign-up-form-container').modal('hide');

					var username = data.username;
					var loginForm = $('#login-form');
					loginForm.find('.login-username').find('input').val(username);
					loginForm.find('.login-password').find('input').val(pass);
					loginForm.submit();
				}
			}
		});
	});
	
	function formCommentElement(data) {
		
		var commentTemplate = $('#comment-template');
        var newComment = commentTemplate.clone();
        newComment.removeAttr('id');
        newComment.removeAttr('style');
        newComment.find('.comment-author').text(data.author);
        newComment.find('.comment-text').text(data.text);
        newComment.find('.comment-date').text(data.prettyPostedWhen);
        
        return newComment;
	}
	
	// Comments posting and likes
	var commentsDiv = $('#comments-div');
	
	$('#webm-view').on('keyup', '#comment-input', function(event) {
		
		var commentInput = $('#comment-input');
		if (event.keyCode == 13 && commentInput.val()) {

			var commentToPost = {text: commentInput.val(), webmId: $('#current-video-id').text()};
			
			console.log('Posting comment:');
			console.log(commentToPost);
			
			$.ajax({
				type: 'POST',
				url: '/comments',
				contentType: 'application/json',
				data: JSON.stringify(commentToPost),
				success: function(data) {
					
	                     console.log('Looks like comment has been posted');
	                     console.log(data);
	                     
	                     commentInput.val('');
	                     commentsDiv.prepend(formCommentElement(data));
				}});
		}
	});
	
	function setCurrentLikeStatus() {
		
		var likeBtn = $('#like-btn');
		if (!likeBtn.length) {
			return;
		}
		
		var currentVideoId = $('#current-video-id').text();
		$.get('/webm/isLiked/' + currentVideoId, function(response) {
			
			console.log('Response for like status:');
			console.log(response);
			
			if (response.status && response.status == 'success') {
				
				if (response.message == 'true') {
					likeBtn.addClass('liked');
				} else {
					likeBtn.removeClass('liked');
				}
			}
		});
	}
	
	function showCommentsForCurrentWebM() {
		
		commentsDiv.find('.comment').not('#comment-template').remove();
		
		var currentVideoId = $('#current-video-id').text();
		$.get('/comments/' + currentVideoId, function(comments) {
			
			for (var i = 0; i < comments.length; i++) {
				commentsDiv.append(formCommentElement(comments[i]));
			}
		});
	}
	
	$('#videos-container').delegate('.webm-preview a', 'click', function(e) {

		e.preventDefault();
		
		$('video', '#webm-view').attr('src', $(this).attr('href'));
		$('#current-video-id').text($(this).parent().find('.webm-id').text());
		$('#video-name').text($(this).parent().find('.webm-name').text());
		$('#author-name').text($(this).parent().find('.webm-author').text());
		$('#video-date').text($(this).parent().find('.webm-postedWhen').text());

		var webmTags = $('#video-tags');
		webmTags.find('.tag').remove();

		var tags = $(this).parent().find('.webm-tags').text().split(/\W+/);
		for (var j = 0; j < tags.length; j++) {

			if (!tags[j].trim()) {
				continue;
			}

			var tagRef = $('<a/>');
			tagRef.attr('href', '/search?keywords=' + tags[j]);
			tagRef.addClass('tag');
			tagRef.text('#' + tags[j]);

			webmTags.append(tagRef);
		}
		
		showCommentsForCurrentWebM();
		setCurrentLikeStatus();
		
		$('#webm-view-container').modal('show');
	});
	
	$('#webm-view').on('click', '#like-btn', function() {
		
		var currentVideoId = $('#current-video-id').text();
		$.ajax({
			type: 'POST',
			url: '/webm/like/' + currentVideoId,
			success: function(response) {
				
                     console.log('Looks like "like" was toggled');
                     console.log(response);
                     
                     var likeBtn = $('#like-btn');
                     
                     if (response.status && response.status == 'success') {
                    	 
                    	 if (response.message == 'added') {
                    		 likeBtn.addClass('liked');
         				} else {
         					 likeBtn.removeClass('liked');
         				}
                     }
			}});
	});

	function makeSearch() {

		var searchString = $('#search-field').val();
		if (!searchString) {
			return;
		}

		window.location.href = '/search?keywords=' + encodeURIComponent(searchString);
	}

	$('#search-button').click(makeSearch);
	$('#search-field').keydown(function(e) {

		if (e.keyCode == 13) {
			makeSearch();
		}
	});
});