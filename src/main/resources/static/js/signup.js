if(document.querySelector("#warning").innerText.length==0){
    document.querySelector("#warning").style.display = "none";

}
else{
    document.querySelector("#warning").style.display = "block";

}

if(document.querySelector("#success").innerText.length==0){
    document.querySelector("#success").style.display = "none";

}
else{
    document.querySelector("#success").style.display = "block";

}


document.querySelector("#create_account").onsubmit=function(event){
    console.log("create account");
    document.querySelector("#success").style.display = "none";
    let username=document.querySelector("#username").value;
    let password=document.querySelector("#password").value;
    let re_password=document.querySelector("#re_password").value;
    // let fname=document.querySelector("#fname").value;
    // let lname=document.querySelector("#lname").value;

    if(username.length==0||password.length==0||re_password.length==0){
        event.preventDefault();
        document.querySelector("#warning").innerText="You need to fill up all the inputs!";
        document.querySelector("#warning").style.display = "block";
    }
    else if(password!=re_password){
        event.preventDefault();
        document.querySelector("#warning").innerText="Two passwords are not matching!";
        document.querySelector("#warning").style.display = "block";
    }

};

document.querySelector("#goback_signin").onclick=function(){
    console.log("Go back!");
    document.querySelector("#warning").style.display = "none";
    document.querySelector("#success").style.display = "none";
    window.location.href="../signin";
};