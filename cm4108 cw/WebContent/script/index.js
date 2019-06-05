//API keys
var mapBoxApiKey="pk.eyJ1Ijoiam9yZGFuc2xlaWdoIiwiYSI6ImNqb3UyeG9xYzEzdXgzeG5ydDV2bHZ4d3MifQ.HZdI3HdXWGXWyd1XRVALuA";
var baseURL="api";
var currUser = "";


try	{
	$(function()
		{
		init();
		}
	);
	} catch (e)
		{
		alert("*** jQuery not loaded. ***");
		}

	
function init(){
	
	var map=makeMap("map",1,0.0,0.0);
	var marker=makeMarker(map,0.0,0.0);
	
// click handlers
$("#Go").click(function(){
		id = $("#myid").val();
		if(id == ""){ //initial input validation 
			alert("Please enter an ID to log on");
		}else{
			alert("Successfully Logged in");
			Go(id);
		}
});

$("#checkin").click(function(){
		if (currUser == ""){ 
			alert("Please Log In First");
		}else{
			checkin(marker);
		}
});

//refresh friends subscriptions
$("#refresh").click(function(){
		if (currUser == ""){
			alert("Please Log In First");
		}else{
			alert("Refreshed")
			populatesubs();
		}
});

$("#send").click(function(){
		if (currUser == ""){
			alert("Please Log In First");
		}else{
			var subscription = $("#friendreq").val();
			send(subscription);
		}
});

//refresh list of incoming friend requests
$("#freq").click(function()
		{
		if (currUser == ""){
			alert("Please Log In First");
		}else{
			freq();
		}
});

$("#app").click(function(){
		if (currUser == ""){
			alert("Please Log In First");
		}else{
			if(confirm("Confirm Request Approval")){ // confirm approval 
				app(marker);
			}
		}
});

$("#deny").click(function(){
		if (currUser == ""){
			alert("Please Log In First");
		}else{
			if(confirm("Confirm Request Denial")){
				deny();
			}
			
		}
});

}


function Go(id){
	//log in in function, create new user
	//if account exists get user data
	var url=baseURL+"/location/"+id;
		
		$.getJSON(url,function(jsonData){
					lat=jsonData["latitude"];
					lon=jsonData["longitude"];
					currUser=jsonData["name"];
				});
}

function checkin(){
	//check in current users location
	latitude = $("#lat").val();
	longitude = $("#long").val();
	date = new Date();
	data = {
			"name": currUser,
			"longitude": longitude,
			"latitude": latitude,
			"date": date,
	};
	
	var url=baseURL+"/location";
	//send user details to location database
	$.post(url, data, function(){
				alert("User Checked In")
			});
}

function populatesubs(){
	//get and display friends
	var url=baseURL+"/location";		

	//get data user data from location database
	$.getJSON(url,function(locations){
			
			$("#locations").empty();		
			for (var i in locations){
				
				var local=locations[i];
				var name=local["name"];	
				//var lat=local["latitude"];
				//var long=local["longitude"];
				var date = local["date"];
				
				//don't display current users name
				if(name != currUser){
					var htmlCode="<li id='"+name+"'>"+name+" checked in on "+date+"</li>";
					$("#locations").append(htmlCode);					
				}
			}			
			}); 
}

function send(subname){
	//send a friend request
	var date = new Date();
	var url=baseURL+"/request";
	var data = {
		"name": currUser,
		"subscription": subname,
		"date": date
	}
	//send data to request database
	$.post(url,data,function(){
				alert ("Request Sent")
			});
}

function freq(){
	
	//get incoming friend requests
	var url=baseURL+"/request/"+currUser;
		//get data about incoming friend request
		$.getJSON(url,function(jsonData){
					$("#requests").empty();
					$.each(jsonData, function(i, item){
						//sends items to function to be displayed 
						displayfreq(item.name, item.date, item.requestID);
					})		
				});
	}
function app(){	
	//approve friend requests 
	$("#requests").children('input').each(function(){
				
		if($(this).prop("checked")) {
			
			var subname = this.value;
			var date = new Date();
			var url= baseURL+"/subscription";
			var requestid = this.id;
			// add user as a subscription
			var data = {
					"name": subname,
					"subname": currUser
			};	
			
				//delete incoming request 
				$.post(	url,data,function(){
							var url=baseURL+"/request/"+requestid;				
							var settings={type:"DELETE"};
							
							$.ajax({
								url:url,
								type:'DELETE',
								success: function(response){
									alert("Request Approved!");
								}
							});						
						});
		}
			
	});
	
}

function deny(){
	//deny incoming friend request
	$("#requests").children('input').each(function(){
		
		if($(this).prop("checked")) {
			//deletes incoming request
			var requestid = this.id;
			var url=baseURL+"/request/"+requestid;				
			var settings={type:"DELETE"};
			
			$.ajax({
				url:url,
				type:'DELETE',
				success: function(response){
					alert("Denied!");
				}
			});
		}	
	});
}

function displayfreq (name, date, id){
	//display incoming friend request
	var requeststring = "<input type='checkbox' id='" + id + "' class='checkbox' name='"+name+"Request' value='"+name+"' >"+name+" ("+date+") <br>"
	console.log(requeststring)
	$("#requests").append(requeststring);
}



function Location(marker){
	//get location of marker
	var longitude=marker.getLatLng().lng;
	var latitude=marker.getLatLng().lat;
	
	$("#long").val(longitude);
	$("#lat").val(latitude);
}

function makeMap(divId,zoomLevel,longitude,latitude){
	
var location=L.latLng(longitude,latitude);		//create location
var map=L.map(divId).setView(location,zoomLevel);	//put map into division
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token='+mapBoxApiKey,
		{attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
		maxZoom: 18,
		id: 'mapbox.streets',
		accessToken: mapBoxApiKey}
		).addTo(map);
return map;	//return map object
} 


//create a marker on a map
function makeMarker(map,longitude,latitude){
	
var location=L.latLng({lon:longitude,lat:latitude});	//create marker at given position
var marker=L.marker(location,{draggable:true});			//make a draggable marker
marker.addTo(map);
marker.on('dragend',function(){
	console.log(this);
	Location(this);
})//add marker to map	
return marker;				//return marker object
}
//end function


