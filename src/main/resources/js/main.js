function emote(json) {
	this.name = json.regex;
	this.url = ko.observable(json.images.url);
	this.tally = ko.observable(0);
}

function fetchemotes(emoteobslist) {
	var req = $.ajax({
		url: "//api.twitch.tv/kraken/chat/emoticons",
		jsonp: "callback",
	  datatype: "jsonp"
	});

	var myemotes = req.done(  function(obj) {
		var data = obj.data
		console.debug(obj);
    emoteobslist(data.emoticons.map(function(item){ return emote(item); }));
	});
}

var viewmodel = {
	emotes: ko.observableArray()
}

$(function(){
	ko.applyBindings(viewmodel);
	fetchemotes(viewmodel.emotes);
});



