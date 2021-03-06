document.addEventListener('click',function(e){
    
    if(e.target && e.target.classList.contains('save')){
          let groupDateName=e.target.parentElement.parentElement.querySelector("button").innerText;
          let event_el=e.target.parentElement.parentElement.parentElement.querySelectorAll("tbody tr");
          let invite_id=e.target.parentElement.parentElement.parentElement.parentElement.dataset.invite_id;
          console.log(invite_id);
          let event_list_temp=[];

          for(let i=0;i<event_el.length;i++){
              let curr_event={};
              let event_id=event_el[i].dataset.event_id;
              curr_event.id=event_id;


              curr_event.preference=event_el[i].querySelector(".preference").value;
              curr_event.availability=event_el[i].querySelector(".availabilty").value;

              event_list_temp.push(curr_event);
          }

          let data={id:invite_id,invite_events_list:event_list_temp};
          console.log(data);
          console.log(JSON.stringify(data));

          $.ajax({
                type: "POST",
                url: "../reply-invite",

                contentType : 'application/json; charset=utf-8',
                dataType : 'json',
                processData: false,
                data: JSON.stringify(data),


          }).done(function(results) {
                    if(results.returnCode=='200'){

                    }

                }).fail(function(results) {
                    // this function runs if the request fails for some reason
                    console.log("API request failed");
                });


     }
 });

function recieveGroupDates(){
    let curr_user = document.querySelector("#brand-name span").innerText;
    document.querySelector("#save_msg").classList.add("noshow");
    $.get('../find-received-invite', { username: curr_user},
        function(returnedData){
            console.log(returnedData);
            for(let i=0;i<returnedData.length;i++){
                let invite_id=returnedData[i].id;
                let inviteName=returnedData[i].inviteName;
                let sender=returnedData[i].sender.username;
                let temp=`<div class="padding" data-invite_id="${invite_id}">
                        <div class="container-fluid ">
                        <div class="row">
                        <div class="col-6 mt-3 mb-3 ">
                        <div class="float-right">
                        <button type="button" class="btn btn-info mr-5 responsive-width" data-toggle="tooltip" data-placement="right" title="Tooltip on right">
                        ${inviteName}
                        </button>
                        </div>
                        </div>
                        
                        <div class="col-6  mt-3 mb-3 ">
                        <button type="button" class="btn btn-primary mr-5 responsive-width save" data-toggle="tooltip" data-placement="right" title="Tooltip on right">
                        Save
                        </button>
                        </div>
                        </div>
                        
                        <div class="row">
                        
                        <div class="col-12 d-flex justify-content-center">
                        <div class="center">
                        
                        <table class="table table-hover table-responsive mt-4">
                        <thead>
                        <tr>
                        <th>Event Name</th>
                        <th>Genre</th>
                        <th>Location</th>
                        <th>Date</th>
                        <th>Sender</th>
                        <th>Preference</th>
                        <th>Availabilty</th>
                        
                        </tr>
                        </thead>
                        <tbody>`;
                let event_list=returnedData[i].invite_events_list;
                for(let j=0;j<event_list.length;j++){
                    let event_id=event_list[j].id;
                    let eventName=event_list[j].eventName;
                    let date=event_list[j].eventDate;
                    let genre=event_list[j].genre;
                    let location=event_list[j].location;
                    let preference=event_list[j].preference;
                    let avil=event_list[j].availability;
                    let prefer_el=`<select name="preference"  class="form-control preference">
                        <option value="5" selected>5</option>
                        <option value="4" >4</option>
                        <option value="3" >3</option>
                        <option value="2" >2</option>
                        <option value="1" >1</option>
                        </select>`;
                    if(preference==1){
                        prefer_el=`<select name="preference"  class="form-control preference">
                        <option value="5" >5</option>
                        <option value="4" >4</option>
                        <option value="3" >3</option>
                        <option value="2" >2</option>
                        <option value="1" selected>1</option>
                        </select>`;
                    }
                    else if(preference==2){
                        prefer_el=`<select name="preference"  class="form-control preference">
                        <option value="5" >5</option>
                        <option value="4" >4</option>
                        <option value="3" >3</option>
                        <option value="2" selected>2</option>
                        <option value="1" >1</option>
                        </select>`;
                    }
                    else if(preference==3){
                        prefer_el=`<select name="preference"  class="form-control preference">
                        <option value="5" >5</option>
                        <option value="4" >4</option>
                        <option value="3" selected>3</option>
                        <option value="2" >2</option>
                        <option value="1" >1</option>
                        </select>`;
                    }
                    else if(preference==4){
                        prefer_el=`<select name="preference"  class="form-control preference">
                        <option value="5" >5</option>
                        <option value="4" selected>4</option>
                        <option value="3" >3</option>
                        <option value="2" >2</option>
                        <option value="1" >1</option>
                        </select>`;
                    }

                    let avl_el=`<select name="availabilty"  class="form-control availabilty">
                        <option value="yes" selected>available</option>
                        <option value="no" >unavailable</option>
                        <option value="maybe">maybe</option>
                        </select>`;
                    if(avil=="no"){
                        avl_el=`<select name="availabilty"  class="form-control availabilty">
                        <option value="yes" >available</option>
                        <option value="no" selected>unavailable</option>
                        <option value="maybe">maybe</option>
                        </select>`;
                    }
                    else if(avil=="maybe"){
                        avl_el=`<select name="availabilty"  class="form-control availabilty">
                        <option value="yes">available</option>
                        <option value="no" >unavailable</option>
                        <option value="maybe"  selected>maybe</option>
                        </select>`;
                    }
                    let el=` <tr data-event_id="${event_id}">
                        <td>${eventName}</td>
                        <td>${genre}</td>
                        <td>${location}</td>
                        <td>${date}</td>
                        <td>${sender}</td>
                        <td>
                        ${prefer_el}
                        </td>
                        <td>
                        ${avl_el}
                        </td>
                        
                        
                        </tr>`;

                    temp=temp+el;
                }

                        
                        

                let end_html=`</tbody>
                               </table>
                               </div>
                               </div> <!-- .col -->
                                </div> <!-- .row -->
                                </div> <!-- .container-fluid -->
                                </div><!-- end padding -->`;
                        
                temp=temp+end_html;
                document.querySelector(".result").innerHTML+=temp;
                        
                        

                        
                        


            }

        }).fail(function(){
        console.log("error");
    });
}

recieveGroupDates();

