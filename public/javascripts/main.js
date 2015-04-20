/*price range*/

 $('#sl2').slider();

	var RGBChange = function() {
	  $('#RGB').css('background', 'rgb('+r.getValue()+','+g.getValue()+','+b.getValue()+')')
	};	
		
/*scroll to top*/

$(document).ready(function(){
	$(function () {
		$.scrollUp({
	        scrollName: 'scrollUp', // Element ID
	        scrollDistance: 300, // Distance from top/bottom before showing element (px)
	        scrollFrom: 'top', // 'top' or 'bottom'
	        scrollSpeed: 300, // Speed back to top (ms)
	        easingType: 'linear', // Scroll to top easing (see http://easings.net/)
	        animation: 'fade', // Fade, slide, none
	        animationSpeed: 200, // Animation in speed (ms)
	        scrollTrigger: false, // Set a custom triggering element. Can be an HTML string or jQuery object
					//scrollTarget: false, // Set a custom target element for scrolling to the top
	        scrollText: '<i class="fa fa-angle-up"></i>', // Text for element, can contain HTML
	        scrollTitle: false, // Set a custom <a> title if required.
	        scrollImg: false, // Set true to use image
	        activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
	        zIndex: 2147483647 // Z-Index for the overlay
		});
	});
});



function nesto(id){
(function() {
	"use strict";

	var infoF = document.getElementById('infoF'+id);
	
	infoF.onclick = function(e) {
		e.preventDefault();

		var infoForm = document.getElementById('infoForm'+id);
		infoForm.style.display = 'block';

		var overlay = document.createElement('div');
		overlay.className = 'overlay';

		var body = document.querySelector('body');
		body.appendChild(overlay);
	};

	
	function closeModal() {
		var modal = this.parentElement;
		modal.style.display = 'none';

		var overlay = document.querySelector('.overlay');
		overlay.parentElement.removeChild(overlay);
	};

	var closeModalButtons = document.querySelectorAll('.close-modal');

	for (var i = 0; i < closeModalButtons.length; i++) {
		closeModalButtons[i].onclick = closeModal;
	}
}());
}

(function() {
	"use strict";

	var infoF = document.getElementById('orderD');

	infoF.onclick = function(e) {
		e.preventDefault();

		var orderDetails = document.getElementById('orderDetails');
		orderDetails.style.display = 'block';

		var overlay = document.createElement('div');
		overlay.className = 'overlay';

		var body = document.querySelector('body');
		body.appendChild(overlay);
	};

	
	function closeModal() {
		var modal = this.parentElement;
		modal.style.display = 'none';

		var overlay = document.querySelector('.overlay');
		overlay.parentElement.removeChild(overlay);
	};

	var closeModalButtons = document.querySelectorAll('.close-modal');

	for (var i = 0; i < closeModalButtons.length; i++) {
		closeModalButtons[i].onclick = closeModal;
	}
}());
