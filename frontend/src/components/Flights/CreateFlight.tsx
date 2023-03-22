import { useState, useEffect, JSXElementConstructor, Key, ReactElement, ReactFragment, ReactPortal } from "react";
import Dialog from "@mui/material/Dialog";
import "./CreateFlight.css";
import axios from "axios";
import { ToastContainer, toast, Flip } from "react-toastify";

interface CreateFlightProps {
  open: boolean;
  onClose: () => void;
}

export default function CreateFlight({ open, onClose }: CreateFlightProps) {
  
  const [date_and_time_taking_off, setDate_and_time_taking_off] = useState("");
  const [place_taking_off, setPlace_taking_off] = useState("");
  const [date_and_time_landing, setDate_and_time_landing] = useState("");
  const [place_landing, setPlace_landing] = useState("");
  const [number_of_tickets, setNumber_of_tickets] = useState(0);
  const [price, setPrice] = useState(0);

  
  var jsonData = {
    date_and_time_taking_off: date_and_time_taking_off,
    place_taking_off: place_taking_off,
    date_and_time_landing: date_and_time_landing,
    place_landing: place_landing,
    number_of_tickets: number_of_tickets,
    price: price
  };
  

  const handleClose = () => {
    onClose();
  };

  const token = localStorage.getItem("token");
  

   const handleCreate = async (e: any) => {
     e.preventDefault();

     const requestOptions = {
         method: "POST",
         headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
  
         body: JSON.stringify(jsonData),
       };
       fetch(
         "http://localhost:8080/api/flight/create/",
         requestOptions,
       ).then((response) => {
         if (response.ok) handleClose();
       });

       console.log(jsonData);
      
     };


    return (    
    <>
        <Dialog onClose={handleClose} open={open}>
          <div className="report-form-wrapper">
            <div className="Auth-form-container dialog">
              <form className="Auth-form" onSubmit={handleCreate}>
                <div className="Auth-form-content">
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Date and time of taking off</label>
                    <input
                      value={date_and_time_taking_off}
                      onChange={(e) => setDate_and_time_taking_off(e.target.value)}
                      required
                      type="datetime-local"
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Place of taking off</label>
                    <input
                      value={place_taking_off}
                      onChange={(e) => setPlace_taking_off(e.target.value)}
                      required
                      type="text"
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Date and time of landing</label>
                    <input
                      value={date_and_time_landing}
                      onChange={(e) => setDate_and_time_landing(e.target.value)}
                      required
                      type="datetime-local"
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Place of landing</label>
                    <input
                      value={place_landing}
                      onChange={(e) => setPlace_landing(e.target.value)}
                      required
                      type="text"
                      className="form-control mt-1"
                      placeholder={""}
                    />
                <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Number of tickets</label>
                    <input
                      value={number_of_tickets}
                      onChange={(e) => setNumber_of_tickets(Number(e.target.value))}
                      required
                      type="number"
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Price</label>
                    <input
                      value={price}
                      onChange={(e) => setPrice(Number(e.target.value))}
                      required
                      type="number"
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="d-grid gap-2 mt-3">
                    <button type="submit" className="btn btn-primary">
                      Submit
                    </button>
                  </div>
                </div>
                </div>
              </form>
            </div>
          </div>
          <ToastContainer
            position="top-right"
            autoClose={5000}
            hideProgressBar
            closeOnClick
            rtl={false}
            pauseOnFocusLoss={false}
            draggable={false}
            pauseOnHover
            limit={1}
            transition={Flip}
          />
        </Dialog>
        </>
      );

}