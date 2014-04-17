var AjaxRequest = {}; //AJAX
var runing = false; //ajax 请求运行状态
AjaxRequest.Get = function (url) {
    var IDs = Array.prototype.slice.apply(arguments).slice(1 || 0);
    var callback = IDs[IDs.length - 1];
    if (typeof callback == 'function') {
        IDs = IDs.slice(0, IDs.length - 1);
    }
    else {
        callback = null;
    }
    if (!runing) {
        $.ajax({
            url: url,
            type: "get",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            beforeSend: function () {
                runing = true;
            },
            success: function (content) {
                if (callback != null)//如果有回调函数
                {
                    callback(content);
                }
            },
            error: function (data, status, statusText) {
            },
            complete: function (jsondata, stat) {
                runing = false;
            }
        });
    }
};
function selectProductTag(m, selfObj, catalogIds, title) {
    //var url = "";
    //if (catalogIds == 0 || catalogIds == 1) {
    //    url = "/ajax/AjaxCacheHandler.aspx?action=ProductCategory_tagContent&mid=" + m;
    //} else {
    //    url = "/ajax/AjaxCacheHandler.aspx?action=ProductCategory&mid=" + m + "&catalogId=" + catalogIds + "&title=" + title;
    //}
    //FindDiv("tagContent" + m, "ProCatContent" + m, url);
    // 操作标签
    var tag = document.getElementById("ProCatTags").getElementsByTagName("li");
    var taglength = tag.length;
    for (i = 0; i < taglength; i++) {
        tag[i].className = "";
    }
    selfObj.parentNode.className = "ProCatSelectTag";
    // 操作内容
    for (i = 0; j = document.getElementById("tagContent" + i) ; i++) {
        j.style.display = "none";
    }
    $("#" + "tagContent" + m).show();
    runing = false;
}
function MM_openBrWindow(theURL, winName, features) {
    window.open(theURL, winName, features);
}
function FindDiv(masterID, sub, url) {
    var masterdiv = $("#" + masterID);
    var subdiv = document.getElementById(sub);
    if (subdiv) { return; }
    if (typeof masterdiv != "undefined") {
        AjaxRequest.Get(url, function (content) {
            $("#" + masterID).append(content);
            $("img.lazy").lazyload();
        });
    }
}
// 图片自适应大小
function adaptImgSize(id, width, height) {
    var src = $("#" + id).attr("src");
    $("#" + id).bind("load", function () {
        $(this).css({ width: "auto", height: "auto" });
        var w = $(this).width();
        var h = $(this).height();
        if (w <= width && h <= height) {
        } else if (w / h > width / height) {
            $(this).css({ width: width + "px", height: "auto" });
        } else {
            $(this).css({ width: "auto", height: height + "px" });
        }
    }).attr("src", src);
}
// 图片自适应大小
function bindAdaptImgSize(obj, width, height) {
    var me = $(obj);
    me.css({ width: "auto", height: "auto" });
    var w = me.width();
    var h = me.height();
    if (w <= width && h <= height) {
    } else if (w / h > width / height) {
        me.css({ width: width + "px", height: "auto" });
    } else {
        me.css({ width: "auto", height: height + "px" });
    }
}
// top
$(document).ready(function () {
    //右边
    var ie6 = !window.XMLHttpRequest;
    var a = document.getElementById('totop-box');
    a.style.position = ie6 ? 'absolute' : 'fixed';
    a.style.right = 5 + "px";
    a.style.bottom = 0;
    var left = document.getElementById('totop-box-left');
    left.style.position = ie6 ? 'absolute' : 'fixed';
    left.style.left = 5 + "px";
    left.style.bottom = 0;
    if (ie6) {
        window.onscroll = function () {
            left.className = left.className;
        };
    }
    function fixed_goback() {
        var y = document.documentElement.scrollTop + document.body.scrollTop;
        if (y > 100) {
            $("#totop-box").show();
            $("#totop-box-left").show();
        } else {
            $("#totop-box").hide();
            $("#totop-box-left").hide();
        }
    }
    $('#totop-box').click(function () { $('html,body').animate({ scrollTop: '0px' }, 200); return false; });
    $('#totop-box-left').click(function () { $('html,body').animate({ scrollTop: '0px' }, 200); return false; });
    $(window).scroll(function () { fixed_goback(); });
    $(window).resize(function () { fixed_goback(); });

    //下拉列表
    $(".select-main").change(function () {
        window.location.href = $(this).val();
    });
    $(".select-left").change(function () {
        window.location.href = $(this).val();
    });
 
});
//obj:当前选中的对象
//noSelectId:没选中的对象
//showId:显示内容块的ID
//hideId:隐藏内容块的描述
function OnmouseOverTab(obj, noSelectId,showId,hideId) {
    $(obj).show().addClass("select-title");
    $("#" + showId).show();
    $("#" + hideId).hide();
    $("#" + noSelectId).removeClass("select-title");
}
//校验方法
function checkEmail(email) {
    var re = new RegExp("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$", "ig");
    return re.test(email)
}
function checkWebSiteUrl(url) {
    var re = new RegExp("^http(s)?://([\w-]+\.)*", "ig");
    return re.test(url);
}
function Get_ID(id) {
    return document.getElementById(id);
}

//leftH：左边高度
//detailsH:详细信息的高度
//moreobj：对象
//descObj：描述信息对象
function detailsHeight(leftH, detailsH, descObj, moreobj) {
    var descDefaultH = leftH - detailsH - 90;//默认显示的描述信息高度
    var showH = descObj.height();//描述信息显示的高度
  
    if (descDefaultH < 0) descDefaultH = 20;
    descObj.css({ "max-height": descDefaultH + "px", "overflow": "hidden" });//初始化高度
    var temp = 0;
    var flipmore = temp;
    if (showH > descDefaultH) {
        moreobj.show();
        descObj.bind('click', function () {
            showDefault(temp, showH, descObj, moreobj, descDefaultH);
            flipmore++
            temp = flipmore;
        });
    } else {
        moreobj.hide();
    }
    var flip = temp;
    moreobj.bind('click', function () {
        showDefault(flip, showH, descObj, moreobj, descDefaultH);
        flip++
        temp = flip;
    });
}
function showDefault(flip, showH, descObj, moreobj, descDefaultH) {
    if (flip% 2 == 0) {
        descObj.css({ "max-height": showH + "px" });
        moreobj.text("Less>>");
    } else {
        descObj.css({ "max-height": descDefaultH + "px", "overflow": "hidden" });
        moreobj.text("More>>");
    }
}


