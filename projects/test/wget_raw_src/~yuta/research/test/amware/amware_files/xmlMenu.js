// ***************************************************************************
function navigate(url)  {
window.location.href = url;
}

// ***************************************************************************
function enterMenuMain(id)  {
if (document.getElementById("MST"+id)) {
    document.getElementById("MST"+id).className = "MST_Hover";
    }
document.getElementById("MMC"+id).className = "MMC_Hover";
document.getElementById("MML"+id).className = "MML_Hover";
}

function leaveMenuMain(id)  {
if (document.getElementById("MST"+id)) {
    document.getElementById("MST"+id).className = "MST_Normal";
    }
document.getElementById("MMC"+id).className = "MMC_Normal";
document.getElementById("MML"+id).className = "MML_Normal";
}

// ***************************************************************************
function enterMenuSub(id)  {
document.getElementById("MSC"+id).className = "MSC_Hover";
document.getElementById("MSL"+id).className = "MSL_Hover";
}

function leaveMenuSub(id)  {
document.getElementById("MSC"+id).className = "MSC_Normal";
document.getElementById("MSL"+id).className = "MSL_Normal";
}

// ***************************************************************************
function enterSideMenu(id)  {
if (document.getElementById("SST"+id)) {
    document.getElementById("SST"+id).className = "SST_Hover";
    }
document.getElementById("SMC"+id).className = "SMC_Hover";
document.getElementById("SML"+id).className = "SML_Hover";
}

function leaveSideMenu(id)  {
if (document.getElementById("SST"+id)) {
    document.getElementById("SST"+id).className = "SST_Normal";
    }
document.getElementById("SMC"+id).className = "SMC_Normal";
document.getElementById("SML"+id).className = "SML_Normal";
}

// ***************************************************************************
function enterSideSub(id)  {
document.getElementById("SSC"+id).className = "SSC_Hover";
document.getElementById("SSL"+id).className = "SSL_Hover";
}

function leaveSideSub(id)  {
document.getElementById("SSC"+id).className = "SSC_Normal";
document.getElementById("SSL"+id).className = "SSL_Normal";
}

// ****************************************************************************
