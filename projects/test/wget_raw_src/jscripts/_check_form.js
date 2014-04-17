function isValidEmailAddress(emailAddress)
{
    var pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
    return pattern.test(emailAddress);
}

function check_obligatory()
{
    oblig_num=0;
    $(".oblig").each(
      function()
      {
          elem_val=$(this).val();
          elem_type=$(this).attr("type");
          elem_name=$(this).attr("name");
          $(this).parent().find("span").remove();
          $(this).parent().find("br").remove();
          if (elem_val!="" && elem_type=="text")
              $(this).css("border-color","");

          if (elem_val=="" && elem_type=="text")
          {
              $(this).css("border-color","red");
              $(this).parent().append('<br/><span style="color: red;">* ��� ���� ���������� ���������.</span>');
              oblig_num++;
          }		  
          if (elem_val!="" && elem_name=="fname_email")
          {
          if (!isValidEmailAddress(elem_val))
          {
              $(this).css("border-color","red");
              $(this).parent().append('<br/><span style="color: red;">* ������ � �������� ������.</span>');
              oblig_num++;
          }
          }

      }
    );
    if (oblig_num==0) $("#send_form").submit();
}
