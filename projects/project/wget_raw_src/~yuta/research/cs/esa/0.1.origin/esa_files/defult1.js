
function tabsClass(value){
var i = 0;
for(i=1;i<=6;i++){
if(value == 'tab'+i){
var names = value;
$('#tab'+i).addClass('tabs_on');
$('#tab'+i).removeClass('tabs_off');
} else {
$('#tab'+i).addClass('tabs_off'); 
$('#tab'+i).removeClass('tabs_on');
}
  if(value == 'tab'+i){
   var names = value;
  }
 }
 
  $('#tab_show').css
  $('#tab_show').html('<div align="center" style="margin-top:30px;"><img src="images/loadings.gif" width="31" height="31" /><br /><strong>Please Wait..</strong></div>');
  $.post('tabs.php', {name:names}, function(data)
         {
    $('#tab_show').html(data);
   });
 }
 
 function fav_links(value,num){
	 var f = 0;
for(f=1;f<=3;f++){
	if(f == num){
		$('#fav'+f).show();
		 	$('#fav-inner'+num).removeClass('fav-inner');
	$('#fav-inner'+num).addClass('fav-inners');
		  $('#fav'+num).html('<div align="center" style="margin-top:30px;"><img src="images/loadings.gif" width="31" height="31" /><br /><strong>Please Wait..</strong></div>');
 $.post('tabs.php', {page:value}, function(data)
         {

    $('#fav'+num).html(data);
   });

	}else{
 	$('#fav-inner'+f).removeClass('fav-inners');		
	$('#fav-inner'+f).addClass('fav-inner');
		$('#fav'+f).hide();		
	}
}
}
 function showmap(vale){
	 if(vale == 'on'){
	 $('#maps_bg').show();		 
	 $('#mapss').show();
	 $('#mapss').html('<iframe src="http://www.easyservicedapartments.com/maps.php" width="100%" height="100%"  ></iframe>');
	 }	 if(vale == 'off'){
	 $('#mapss').hide();		 
	 $('#maps_bg').hide();
	 }

 }
 
function get_tweets(user){
if(user){
$('#tweet').html('<div align="center" style="margin-top:30px;"><img src="images/loadings.gif" width="31" height="31" /><br /><strong>Loading Tweets..</strong></div>');
$.post('tweetfeed.php', {name:user}, function(data) {
$('#tweet').html(data);
	}); 
	
	}	
  }
  
function get_blog_records(name){
if(name){
$('#blog').html('<div align="center" style="margin-top:30px;"><img src="images/loadings.gif" width="31" height="31" /><br /><strong>Loading Blog Posts..</strong></div>');
$.post('wp_post.php', {name:name}, function(data) {
$('#blog').html(data);
	}); 
	
	}	
  }
