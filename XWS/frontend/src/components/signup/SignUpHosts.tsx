import React, { FC } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import { ToastContainer, toast, Flip } from "react-toastify";
import AuthWrapper from "../wrapper/AuthWrapper";
import { useState, useEffect, JSXElementConstructor, Key, ReactElement, ReactFragment, ReactPortal } from "react";
import Dialog from "@mui/material/Dialog";
import "./SignUpHosts.css";

interface SignUpHostsProps {
  open: boolean;
  onClose: () => void;
}

export default function SignUpHosts({ open, onClose }: SignUpHostsProps) {
  
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [first_name, setFirst_name] = useState("");
  const [last_name, setLast_name] = useState("");
  const [address, setAddress] = useState("");
  
  var jsonData = {
    username: username,
    email: email,
    password: password,
    first_name: first_name,
    last_name: last_name,
    address: address
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
         process.env.REACT_APP_URL+"auth/signuphost",
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
                    <label style={{ textTransform: "capitalize" }}>Username</label>
                    <input
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      required
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>First name</label>
                    <input
                      value={first_name}
                      onChange={(e) => setFirst_name(e.target.value)}
                      required
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Last name</label>
                    <input
                      value={last_name}
                      onChange={(e) => setLast_name(e.target.value)}
                      required
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Address</label>
                    <input
                      value={address}
                      onChange={(e) => setAddress(e.target.value)}
                      required
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Email</label>
                    <input
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                      type="email"
                      className="form-control mt-1"
                      placeholder={""}
                    />
                  </div>
                  <div className="form-group mt-3 divSize50L">
                    <label style={{ textTransform: "capitalize" }}>Password</label>
                    <input
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      required
                      type="password"
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