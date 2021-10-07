document.querySelector("#signup").onclick=function(){
    window.location.href="../signup";
};

if(document.querySelector("#warning").innerText.length==0){
    document.querySelector("#warning").style.display = "none";

}
else{
    document.querySelector("#warning").style.display = "block";

}


document.querySelector("#login").onsubmit=function(event){
    document.querySelector("#warning").style.display = "none";
    let username=document.querySelector("#username").value;
    let password=document.querySelector("#password").value;


    if(username.length==0||password.length==0){
        event.preventDefault();
        document.querySelector("#warning").innerText="Username or Password is empty!";
        document.querySelector("#warning").style.display = "block";
    }


};