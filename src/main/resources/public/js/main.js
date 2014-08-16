function emote(json) {
	this.name = json.regex;
	this.url = ko.observable(json.images[0].url);
	this.tally = ko.observable(0);
	this.changed = ko.observable(false);
}

function fetchemotes(emoteobslist) {
	var req = $.ajax({
		url: "/emoticons",
	  datatype: "json"
	});


	var myemotes = req.done(  function(obj) {
		var data = JSON.parse(obj)
		console.debug(data);
    emoteobslist(data.emoticons.slice(0, 1000).map(function(item){ return new emote(item); }));
	});
}

var viewmodel = {
	emotes: ko.observableArray()
}

$(function(){
	ko.applyBindings(viewmodel);
	fetchemotes(viewmodel.emotes);
});



