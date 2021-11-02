function processResponse(obj){
    
    
    let total_results=obj.length;
    

    if(total_results==0){
        document.querySelector("#errmsg").innerHTML="Cannot find anything with this keyword!";
        document.querySelector("#errmsg").classList.remove("noshow");
    }

    
    

    let movies_element=``;

    for(let i=0;i<total_results;i++){
        let cur_obj=obj[i];
        
        console.log(cur_obj);
        let title=cur_obj.name;
        
        let image=cur_obj.images[1].url;

        let date=cur_obj.dates.start.localDate;

        let genre=cur_obj.classifications[0].segment.name;

        
        

        

        let cur_movie_el=`<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
        <div class="parent" data-genre="${genre}">
            <img src="${image}"\>
            <div class="hide">
               Genre: ${genre}
            </div>
        </div>
        <div class="event-name" data-name="${title}">${title}</div>
        <div data-date="${date}">${date}</div>
        <div class="button-size">
        <button type="button" class="btn btn-primary add-event">Add</button>
        </div>
        </div>`;

        movies_element+=cur_movie_el;
    }


   

    if(total_results>0){
        document.querySelector(".movies").innerHTML=movies_element;
    }
};






document.querySelector("#searchbutton").onclick=function(event){
    event.preventDefault();
    let genre=document.querySelector("#genre-select").value;
    let city=document.querySelector("#city-name").value;
    let date=document.querySelector("#choose-date").value;
    
    let keyword=document.querySelector("#keyword").value;

    if(genre.length>0&&city.length>0&&date.length>0){
        
        document.querySelector("#errmsg").classList.add("noshow");
        let endpoint="https://app.ticketmaster.com/discovery/v2/events.json?size=12&city="+city+"&keyword="+keyword+"&classificationName="+genre+"&startEndDateTime="+date+"T12:00:00Z&apikey=f3kuGY9d20QsgYWlhdQ0PwnnkeU2Mqym";
        console.log(endpoint)

        let httpRequest = new XMLHttpRequest();
        httpRequest.open("GET",endpoint);
        httpRequest.send();

        httpRequest.onreadystatechange=function(){
            if(httpRequest.readyState==4){
                if(httpRequest.status==200){
                    let response_json=JSON.parse(httpRequest.responseText);
                    //console.log(response_json._embedded.events);
                    processResponse(response_json._embedded.events);
                    
                }
                else{
                    //console.log(httpRequest.readyState);
                    
                }
            }
        };

    }
    else{
        document.querySelector("#errmsg").innerHTML="There is input text is empty!";
        document.querySelector("#errmsg").classList.remove("noshow");
    }
    
   
};


$('body').on('click', '.add-event', function() {
    let event=$(this).parent().parent().children();
    
    
    
    let genre=event.eq(0).data("genre");
    let name=event.eq(1).data("name");
    let date=event.eq(2).data("date");
    
    let temp=document.querySelector("#add-events-list").innerHTML;

    let el=`<div class="alert alert-warning alert-dismissible fade show list-button" role="alert" data-genre="${genre}" data-name="${name}" data-date="${date}">
    <div class="list-info"><strong>${name}</strong></div> 
    <div class="list-info">${genre}</div> 
    <div class="list-info">${date}</div> 
    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
    </div>`;

    document.querySelector("#add-events-list").innerHTML=temp+el;


});



document.querySelector("#searchusername").onkeyup=function(){
    if(this.value.length>0){
        let data={name:this.value};
        console.log(this.value);

        $.ajax({
            type: "POST",
            url: "../usernameStartingWith",

            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            processData: false,
            data: JSON.stringify(data),


        })
            .done(function(results) {
                // this function runs when we get a response from itunes API
                

                let terms=results.names;

                let res = document.getElementById("result");
                res.innerHTML = '';
                let list = '';

                for (let i=0; i<terms.length; i++) {
                    list += '<li>' + terms[i] + '</li>';
                }
                res.innerHTML = '<ul>' + list + '</ul>';
            })
            .fail(function(results) {
                // this function runs if the request fails for some reason
                console.log("API request failed");
            });
    }


}


$('body').on('click', 'li', function() {
    let select=$(this).text();
    document.querySelector("#searchusername").value=select;


});


document.querySelector("#username_add").onclick=function(){
    let username=document.querySelector("#searchusername").value;
    let msg=document.querySelector("#username_errmsg");
    if(!msg.classList.contains("noshow")){
        msg.classList.add("noshow");
    }
    if(username.length==0){

        msg.innerHTML="Input Username is Empty!";
        msg.classList.remove("noshow");
    }
    else{
        let terms=document.querySelectorAll("#result li");

        if(terms.length==0){
            msg.innerHTML="Username does not exist!";
            msg.classList.remove("noshow");
        }
        else{

            let usernames=document.querySelectorAll("#senders_list .list-info");
            let found=false;
            for(let i=0;i<usernames.length;i++){
                console.log(usernames[i].innerText);
                if(usernames[i].innerText==username){
                    found=true;
                    break;
                }
            }

            if(found){
                msg.innerHTML="Username already exists in the sender list!";
                msg.classList.remove("noshow");
            }
            else{
                let temp=document.querySelector("#senders_list").innerHTML;

                let el=`<div class="alert alert-warning alert-dismissible fade show list-button" role="alert" data-username="${username}">
                <div class="list-info"><strong>${username}</strong></div> 
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
                </div>`;

                document.querySelector("#senders_list").innerHTML=temp+el;
            }


            //console.log(document.querySelectorAll("#senders_list .list-info"));

        }

    }

};