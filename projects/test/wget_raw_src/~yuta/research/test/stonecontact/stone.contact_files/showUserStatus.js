function showUserStatus_mainSite(userStatuDiv_id)
{
    var userEmail = getCookie("___LastLoginEmailCookieName");
    var userName = getCookie("ST_DisplayName");
    var htmlContent;
    if (userEmail != null)
    {
        if (userEmail.length > 18) {
            userEmail = userEmail.substring(0, 15) + '...';
        }
        htmlContent  = "";
        if (userName == null) {

            htmlContent += "<a rel='nofollow' href='/Main.aspx?pid=Office_Login'class=\"import\">Welcome, " + userEmail + ",<b> Sign in</b></a>";
        }
        else {
            htmlContent += "<a rel='nofollow' href='/Main.aspx?pid=MemberCenter'class=\"import\">Welcome, " + userEmail + ",<b> My StoneContact</b></a>";
            htmlContent += "                    <a href='/Main.aspx?pid=Office_Login&id=Logout' id=\"Sign\">Logout</a>";
        }
  
    }
    else
    {
        htmlContent  = "";        
        htmlContent += "                    <a rel='nofollow' href='/Main.aspx?pid=Office_Login' id=\"Sign\">Sign in</a> ";
        htmlContent += "                    <a rel='nofollow' href=\"/Main.aspx?pid=Office_Register\" id=\"Join\">Join Free Now</a>";
    }
    document.getElementById(userStatuDiv_id).innerHTML = htmlContent;   
}
