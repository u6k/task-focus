$(function() {
	var dataMarkdown = $("#task-description").attr("data-markdown");
	if (typeof dataMarkdown !== typeof undefined && dataMarkdown !== false) {
		var markedOptions = {
			sanitize: true
		};
		$("#task-description").append(marked(dataMarkdown, markedOptions));
	} else {
		$("#task-description").append(" ");
	}
});
