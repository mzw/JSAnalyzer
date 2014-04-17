Event.observe(window, 'load', function(e){
	//tabset
	var para = {
		animate: {opacity: .9999},
		onShow: function(elem) { 
			elem.element.previous().addClassName('active')
		},
		onHide: function(elem) { 
			elem.element.previous().removeClassName('active')
		}
	}
	if($('productTabset')){
		$('productTabset').tabset(para).showItem(0);
	}	

	//sliders in tabset
	$$('.productSlider').each(function(v){
		new Df.Slider(v).set({
			iterateBy: 174*3	
		});
	});
	
/*
	// expanding/contracting sub categories
	var maxHeight = 77
	var elePadding = 5
	var holder = $$('.catListHead').each(function(v){
		ele = v.select('ul')[0]
		ele.setStyle({display:'block'})
		if(ele.scrollHeight > maxHeight && ele.childElements().length > 5){
			ele.setStyle({height: (maxHeight - elePadding) + 'px'})
			
			ele.insert({after:'<div class="seeAllLink"><span>More Categories</span></div>'})
			
			ele.next().observe('click', function(){
				if(Object.isUndefined(this.expandedFlag) || this.expandedFlag == false) {
					this.animate({
						height: this.scrollHeight,
						onComplete: function(){
							this.next().innerHTML = '<span>Less Categories</span>'
							this.expandedFlag = true
						}.bind(this)
					})
				} else {
					this.animate({
						height: (maxHeight - elePadding),
						onComplete: function(){
							this.next().innerHTML = '<span>More Categories</span>'
							this.expandedFlag = false
						}.bind(this)
					})
				}
			}.bind(ele))
		}else{
			ele.setStyle({height:ele.scrollHeight+'px'})
		}
	});
*/

	var setEleHeight = function(elems){
		if($$(elems)) {
			var eleHeight = $$(elems).max(function(v){
				return v.offsetHeight;
			})
	
			$$(elems).each(function(v){
				v.setStyle({height: eleHeight})
			})
		}
	}
	setEleHeight('div.featProds1 div.catListHead .catListTitle')
	setEleHeight('div.featProds2 div.catListHead .catListTitle')
});	
