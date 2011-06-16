/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$j = jQuery.noConflict();

$j(document).ready(function(){
                
    alert('Step 1');
                
    $j('textarea').each(function () {
        $j(this).addClass("bubbleinfo");
    });
    $j('input[type="text"]').each(function () {
        $j(this).addClass("bubbleinfo");
    });

    $j('.bubbleinfo').CreateBubblePopup();
                
    $j('.bubbleinfo').each(function () {
        alert('Step 2');
        var input = $j(this);
        $j(this).focus(function() {
                        
            if(this.title == '') {
                this.title = this.name;
            }
                        
            var title = this.title + '<br/>';
            //show the bubble popup with new options
            input.ShowBubblePopup({
                innerHtml: /*bubble_popup_open_on + bubble_popup_creation + bubble_popup_last_display*/ title,
	
                innerHtmlStyle: {
                    color:'#333333', 
                    'text-align':'center'
                },
										
                themeName: 	'orange',
                themePath: 	'../layouts/images/jquerybubblepopup-theme'								 
            });
        }).blur(function() {
            input.HideBubblePopup();
        });
    });

    alert('Step 3');

    //create bubble popups for each element with class "button"
    //set customized mouseover event for each button
    $j('.bubbleinfo').mouseover(function(){
	alert('Step 4');
        //get a reference to the object button
        var button = $(this);
        if(this.title == '') {
            this.title = this.name;
        }
        var title = this.title + '<br/>';
                   
        //show the bubble popup with new options
        button.ShowBubblePopup({
            innerHtml: title,
	
            innerHtmlStyle: {
                color:'#333333', 
                'text-align':'center'
            },
										
            themeName: 	'orange',
            themePath: 	'../layouts/images/jquerybubblepopup-theme'								 
        });
	
    }); //end mouseover event
                 
});