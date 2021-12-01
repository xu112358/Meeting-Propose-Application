document.querySelector("#finalize").onclick=function (){
    console.log("finalize");
    let shadow=document.querySelector("#shadow");
    console.log(shadow.classList);
    shadow.classList.remove("noshow");
    console.log(shadow.classList);

    let arr=window.location.href.split("=");

    let invite_id=parseInt(arr[arr.length-1]);

    $.post('../propose-finalize-invite', { invite_id: invite_id},
        function(returnedData){
            console.log(returnedData);
            document.querySelector("#average").innerHTML="Average: "+returnedData.average;
            document.querySelector("#median").innerHTML="Median: "+returnedData.median;
            document.querySelector("#opt-event").innerHTML="Optimized Event: "+returnedData.proposedEvent.eventName;
            document.querySelector("#opt-event-date").innerHTML="Event Date: "+returnedData.proposedEvent.eventDate;


            document.querySelector("#finalize_yes").href="../set-finalized-event?inviteId="+invite_id+"&eventId="+returnedData.proposedEvent.id;
        }).fail(function(){
        console.log("error");
    });
};

document.querySelector("#finalize_no").onclick=function(){
    let shadow=document.querySelector("#shadow");
    shadow.classList.add("noshow");
};
