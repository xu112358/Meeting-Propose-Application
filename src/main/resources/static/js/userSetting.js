function getBlockedUsers(){
    let curr_user = document.querySelector("#brand-name span").innerText;
    $.get('../get-blocked-user', { username: curr_user},
        function(returnedData){
            console.log(returnedData);
            let temp='';
            for(let i=0;i<returnedData.blocked_usernames.length;i++){
                let el=`<tr><td>${returnedData.blocked_usernames[i]}</td><td><button class="btn btn-outline-danger delete-btn removeUser">Remove</button></td></tr>`;
                temp=temp+el;
            }

            document.querySelector("#blockedUsers").innerHTML=temp;

        }).fail(function(){
        console.log("error");
    });
}

getBlockedUsers();

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
    document.querySelector("#searchusername").value=select.split("/")[0];


});


document.querySelector("#username_add").onclick=function() {
    let username = document.querySelector("#searchusername").value;
    let sender = document.querySelector("#brand-name span").innerText;


    let msg = document.querySelector("#username_errmsg");


    if (username == sender) {
        msg.innerHTML = "You cannot block yourself!";
        msg.classList.remove("noshow");
        return;
    }
    if (!msg.classList.contains("noshow")) {
        msg.classList.add("noshow");
    }


    if (username.length == 0) {

        msg.innerHTML = "Input Username is Empty!";
        msg.classList.remove("noshow");
        return;
    } else {
        let terms = document.querySelectorAll("#result li");

        if (terms.length == 0) {
            msg.innerHTML = "Username does not exist!";
            msg.classList.remove("noshow");
            return;
        } else {
            let match = false;

            for (let i = 0; i < terms.length; i++) {


                if (username == terms[i].innerText.split("/")[0]) {
                    match = true;
                    break;
                }
            }

            if (!match) {
                msg.innerHTML = "Username does not exist!";
                msg.classList.remove("noshow");
                return;
            }
        }
    }



    $.post('../add-blocked-user', { username: sender, block : username},
        function(returnedData){
            if(returnedData.returnCode=="200"){
                getBlockedUsers();
            }
            else if(returnedData.returnCode=="400"){
                msg.innerHTML = returnedData.message;
                msg.classList.remove("noshow");
                return;
            }
        }).fail(function(){
        console.log("error");
    });


};

document.addEventListener('click',function(e){

    if(e.target && e.target.classList.contains('removeUser')){
        let confirmText = "Are you sure you want to remove this blocked user?";
        if(confirm(confirmText)) {

            let blocked = e.target.parentElement.parentElement.querySelector("td").innerText;
            let curr_user = document.querySelector("#brand-name span").innerText;

            $.post('../delete-blocked-user', { username: curr_user, blocked: blocked},
                function(returnedData){
                    if(returnedData.returnCode=="200"){
                        getBlockedUsers();
                    }

                }).fail(function(){
                console.log("error");
            });
        }
    }
});


document.querySelector("#updateDate").onsubmit=function(event){

    event.preventDefault();
    let startDate=document.querySelector("#start-date").value;
    let endDate=document.querySelector("#end-date").value;
    let msg=document.querySelector("#daterange_errmsg");
    msg.classList.add("noshow");
    if(startDate.length==0||endDate.length==0){
        msg.innerText="Start Date or End Date is Empty!";
        msg.classList.remove("noshow");
        return;
    }

    if(startDate>endDate){
        console.log("The Date Range is Invalid!");
        msg.innerText="The Date Range is Invalid!";
        msg.classList.remove("noshow");
        return;
    }

    event.target.submit();



};