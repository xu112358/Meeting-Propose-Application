document.addEventListener('click',function(e){
    
    if(e.target && e.target.classList.contains('save')){
          let td=e.target.parentElement.parentElement.parentElement.querySelectorAll("tr td");
          
          for(let i=0;i<td.length;i++){
              if(i%7==5 || i%7==6){
                console.log("value:");  
                console.log(td[i].querySelector("select").value);
              }
             
          }
     }
 });

function recieveGroupDates(){
    let curr_user = document.querySelector("#brand-name span").innerText;
    $.get('../find-received-invite', { username: curr_user},
        function(returnedData){
            console.log(returnedData);
            for(let i=0;i<returnedData.length;i++){
                console.log(returnedData[i]);
            }

        }).fail(function(){
        console.log("error");
    });
}

recieveGroupDates();

