
$(function () {
    var delay = 200;
    var allCateTimer = null;
    $('div#nav > ul > li').hover(
        function (ev) {
            var $this = this;
            allCateTimer = setTimeout(function () {
                var bottomHeight = document.documentElement.clientHeight - ev.clientY;
                if (bottomHeight <= 250) {
                    $($this).addClass('over').find('div.submenubox').addClass('submenuboxBottom').removeClass('disn');
                } else {
                    $($this).addClass('over').find('div.submenubox').removeClass('submenuboxBottom').removeClass('disn');
                }
            }, delay);
        },
        function () {
            var $this = this;
            if (allCateTimer) {
                clearTimeout(allCateTimer);
            }
            allCateTimer = setTimeout(function () {
                $($this).removeClass('over').find('div.submenubox').addClass('disn');
            }, delay);
        }
    );
});
var lastLi;
function ShowPopMenu(ele) {
    $('li.floatMenu').removeClass('current hover');
    var pos = $(ele).position();
    var cid = $(ele).attr('menu');
    var w = $(ele).width() - 6;
    var bottomHeight = ele.offsetTop;
    if (w <= 0 && screen.width > 1024) {
        w = 204;
        bottomHeight = 0;
    } else if (w <= 0 && screen.width <= 1024) {
        w = 186;
        bottomHeight = 0;
    } else {
        bottomHeight = 182;
    }
    //$('#' + cid).css('left', (ele.offsetLeft + w) + 'px').css('top', bottomHeight + 'px').show();
    $('#' + cid).show();
    $(ele).addClass('current hover');
    lastLi = $(ele);
}

function HidePopMenu(ele) {
    $('li.floatMenu').removeClass('current hover');
    $('#' + $(ele).attr('menu')).hide();
}

function OverPop(ele) {
    if (lastLi && lastLi.attr('menu') == ele.id) {
        lastLi.addClass('current hover');
    }
    $(ele).show();
}

function OutPop(ele) {
    $(ele).hide();
    $('li.floatMenu').removeClass('current hover');
}
