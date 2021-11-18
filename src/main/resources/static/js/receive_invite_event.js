document.addEventListener('change',function(e) {

    if (e.target && (e.target.classList.contains('preference')||e.target.classList.contains('availability'))) {
        let tr_el=e.target.parentElement.parentElement;
        let select_el=tr_el.querySelectorAll("select");
        let event_id=tr_el.dataset.eventid


        $.get('../update_receive_invite_events', { eventId: event_id, preference: select_el[0].value,availability:select_el[1].value},
            function(returnedData){
                if(returnedData.message=="Updated"){
                    console.log(returnedData);
                }

            }).fail(function(){
            console.log("error");
        });

    }
});