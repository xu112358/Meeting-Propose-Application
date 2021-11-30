function draft_init(){
    let cur_user=localStorage.getItem("cur_user");
    let cur_username=document.querySelector("#brand-name span").innerHTML;
    if(cur_user==null||cur_username!=cur_user){
        localStorage.clear();
        localStorage.setItem("cur_user",cur_username);
        let senders=[];
        let events=[];
        localStorage.setItem("senders",JSON.stringify(senders));
        localStorage.setItem("events",JSON.stringify(events));
    }
    else{
        let events=JSON.parse(localStorage.getItem("events"));

        for(let i=0;i<events.length;i++){
            let genre=events[i].genre;
            let location=events[i].location;
            let name=events[i].name;
            let date=events[i].date

            let temp=document.querySelector("#add-events-list").innerHTML;

            let el=`<div class="events" role="alert" data-genre="${genre}" data-name="${name}" data-date="${date}" data-location="${location}">
            <button class="remove" >
               x
            </button>
            <div class="list-info"><strong>${name}</strong></div> 
            <div class="list-info">${genre}</div> 
            <div class="list-info">${date}</div> 
            <div class="list-info">${location}</div> 
            
            </div>`;

            document.querySelector("#add-events-list").innerHTML=temp+el;
        }

        let users=JSON.parse(localStorage.getItem("senders"));
        for(let j=0;j<users.length;j++){
            let username=users[j];
            let temp=document.querySelector("#add-users-list").innerHTML;

            let el=`<div class="users"  data-username="${username}">
                <button class="remove" >
                  x
                </button>
                <div class="list-info"><strong>${username}</strong></div> 
                
                </div>`;

            document.querySelector("#add-users-list").innerHTML=temp+el;
        }
    }
};

draft_init();

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
        if(title.length>20){
            title=title.substring(0,20)+"...";
        }
        
        let image=cur_obj.images[1].url;

        let date=cur_obj.dates.start.localDate;

        let genre=cur_obj.classifications[0].segment.name;

        
        let city=document.querySelector("#city-name").value;

        

        let cur_movie_el=`<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
        <div class="parent" data-genre="${genre}" data-city="${city}">
            <img src="${image}"\>
            <div class="hide">
               Genre: ${genre}<br>
               Location: ${city}
            </div>
        </div>
        <div class="event-name" data-name="${title}">${title}</div>
        <div data-date="${date}" class="event-date">${date}</div>
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
    let location=event.eq(0).data("city");
    let name=event.eq(1).data("name");
    let date=event.eq(2).data("date");
    
    let temp=document.querySelector("#add-events-list").innerHTML;

    let el=`<div class="events" role="alert" data-genre="${genre}" data-name="${name}" data-date="${date}" data-location="${location}">
    <button class="remove" >
       x
    </button>
    <div class="list-info"><strong>${name}</strong></div> 
    <div class="list-info">${genre}</div> 
    <div class="list-info">${date}</div> 
    <div class="list-info">${location}</div> 
    
    </div>`;

    document.querySelector("#add-events-list").innerHTML=temp+el;

    let temp_map={};
    temp_map.name=name;
    temp_map.genre=genre;
    temp_map.location=location;
    temp_map.date=date;
    let events=JSON.parse(localStorage.getItem("events"));
    events.push(temp_map);
    localStorage.setItem("events",JSON.stringify(events));
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
    let sender=document.querySelector("#brand-name span").innerText;



    let msg=document.querySelector("#username_errmsg");


    if(username==sender){
        msg.innerHTML="You cannot add yourself to the sender users list!";
        msg.classList.remove("noshow");
        return;
    }
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
            let match=false;

            for(let i=0;i<terms.length;i++){


                if(username==terms[i].innerText){
                    match=true;
                    break;
                }
            }

            if(!match){
                msg.innerHTML="Username does not exist!";
                msg.classList.remove("noshow");
                return;
            }

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
                let temp=document.querySelector("#add-users-list").innerHTML;

                let el=`<div class="users"  data-username="${username}">
                <button class="remove" >
                  x
                </button>
                <div class="list-info"><strong>${username}</strong></div> 
                
                </div>`;

                document.querySelector("#add-users-list").innerHTML=temp+el;

                let senders=JSON.parse(localStorage.getItem("senders"));
                senders.push(username);
                localStorage.setItem("senders",JSON.stringify(senders));
            }


            //console.log(document.querySelectorAll("#senders_list .list-info"));

        }

    }

};


document.querySelector("#propose-events").onclick=function(){
    let sender=document.querySelector("#brand-name span").innerText;
    let groupDate_name=document.querySelector("#groupDate_name").value;
    let recievers_el=document.querySelectorAll("#add-users-list .list-info");
    let events_el=document.querySelectorAll("#add-events-list .list-info");

    document.querySelector("#proposeEvent_success").classList.add("noshow");
    document.querySelector("#proposeEvent_errmsg").classList.add("noshow");

    if(groupDate_name.length==0||recievers_el.length==0||events_el.length==0){
        document.querySelector("#proposeEvent_errmsg").classList.remove("noshow");
    }
    else{

        let recievers=[];

        for(let i=0;i<recievers_el.length;i++){
            recievers.push(recievers_el[i].innerText);
        }

        let events=[];
        let event_temp={};
        for(let i=0;i<events_el.length;i++){
            if(i%4==0){
                event_temp={};
                event_temp.eventName=events_el[i].innerText;
            }
            else if(i%4==1){
                event_temp.genre=events_el[i].innerText;
            }
            else if(i%4==2){
                event_temp.eventDate=events_el[i].innerText;
            }
            else if(i%4==3){
                event_temp.location=events_el[i].innerText;
                events.push(event_temp);
            }

        }



        let data={sender:sender,events:events,receivers:recievers,invite_name:groupDate_name};
        console.log(data);

        $.ajax({
            type: "POST",
            url: "../send-invite",

            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            processData: false,
            data: JSON.stringify(data),


        })
            .done(function(results) {
                console.log(results);
                if(results.message=="Invite Sent"){
                    document.querySelector("#proposeEvent_success").classList.remove("noshow");
                }
            })
            .fail(function(results) {
                // this function runs if the request fails for some reason
                console.log("API request failed");
            });
    }




};


document.addEventListener('click',function(e){

    if(e.target && e.target.classList.contains("remove")){
        let confirmText = "Are you sure you want to delete this user or event?";
        if(confirm(confirmText)) {
            let username=e.target.parentElement.dataset.username;
            let event_name=e.target.parentElement.dataset.name;
            let genre=e.target.parentElement.dataset.genre;
            let date=e.target.parentElement.dataset.date;
            let location=e.target.parentElement.dataset.location;


            if(username===undefined){ // events list remove
                let events=JSON.parse(localStorage.getItem("events"));
                for (let i =0; i < events.length; i++){
                    if (events[i].name == event_name && events[i].genre == genre && events[i].date == date && events[i].location == location) {
                        events.splice(i,1);
                        break;
                    }
                }

                localStorage.setItem("events",JSON.stringify(events));
            }
            else{ // senders list remove
                let senders=JSON.parse(localStorage.getItem("senders"));
                senders = senders.filter(e => e !== username);
                localStorage.setItem("senders",JSON.stringify(senders));
            }
            e.target.parentElement.remove();


        }

    }
});